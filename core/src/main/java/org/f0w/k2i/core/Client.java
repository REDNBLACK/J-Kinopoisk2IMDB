package org.f0w.k2i.core;

import com.google.common.eventbus.EventBus;
import com.google.gson.JsonObject;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.google.inject.persist.PersistService;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.NonNull;
import org.f0w.k2i.core.event.ImportFinishedEvent;
import org.f0w.k2i.core.event.ImportProgressAdvancedEvent;
import org.f0w.k2i.core.event.ImportStartedEvent;
import org.f0w.k2i.core.handler.MovieHandler;
import org.f0w.k2i.core.ioc.ConfigurationModule;
import org.f0w.k2i.core.ioc.JpaRepositoryModule;
import org.f0w.k2i.core.ioc.ServiceModule;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.service.ImportProgressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.f0w.k2i.core.handler.MovieHandler.Type.COMBINED;
import static org.f0w.k2i.core.handler.MovieHandler.Type.SET_RATING;
import static org.f0w.k2i.core.util.IOUtils.checkFile;

/**
 * Main facade implemented as {@link Runnable} for working with core module.
 */
public final class Client implements Runnable {
    protected static final Logger LOG = LoggerFactory.getLogger(Client.class);

    private final Path ratingsHelperPath = Paths.get(System.getProperty("user.home"), "K2IDB", "ratings.json");
    private static final Set<MovieHandler.Type> RATING_MODES = Collections.unmodifiableSet(EnumSet.of(SET_RATING, COMBINED));
    private final MovieHandler.Type handlerType;
    private final MovieHandler handlerChain;
    private final EventBus eventBus;
    private final ImportProgressService importProgressService;
    private final PersistService persistService;

    private final Path filePath;
    private final boolean cleanRun;
    private final Config config;

    public Client(@NonNull Path filePath, @NonNull Config config) {
        this(filePath, config, false);
    }

    public Client(@NonNull Path filePath, @NonNull Config config, boolean cleanRun) {
        this(filePath, config, cleanRun, Collections.emptyList());
    }

    public Client(@NonNull Path filePath, @NonNull Config config, boolean cleanRun, @NonNull List<?> listeners) {
        this.filePath = checkFile(filePath);
        this.cleanRun = cleanRun;
        this.config = config;

        Injector injector = Guice.createInjector(
                Stage.PRODUCTION,
                new ConfigurationModule(ConfigValidator.checkValid(config.withFallback(ConfigFactory.load()))),
                new JpaRepositoryModule(),
                new ServiceModule()
        );

        persistService = injector.getInstance(PersistService.class);
        persistService.start();

        handlerType = injector.getInstance(MovieHandler.Type.class);
        handlerChain = injector.getInstance(MovieHandler.class);
        importProgressService = injector.getInstance(ImportProgressService.class);

        eventBus = new EventBus();
        listeners.forEach(eventBus::register);
    }

    @Override
    public void run() {
        if (cleanRun) {
            importProgressService.deleteAll(filePath);
        }

        List<ImportProgress> importProgress = importProgressService.findOrSaveAll(filePath, config);

        eventBus.post(new ImportStartedEvent(importProgress.size()));

        final List<MovieHandler.Error> allErrors = new ArrayList<>();

        String mode = config.getString("mode");
        MovieHandler.Type type = MovieHandler.Type.valueOf(mode);
        JsonObject json = new JsonObject();

        try {
            for (ImportProgress current : importProgress) {
                List<MovieHandler.Error> currentErrors = new ArrayList<>();

                handlerChain.handle(current, currentErrors, handlerType);

                if (RATING_MODES.contains(type)) {
                    if (current.getMovie().getImdbId() != null) {
                        json.addProperty(current.getMovie().getImdbId(), current.getMovie().getRating());
                    }
                }

                eventBus.post(new ImportProgressAdvancedEvent(current, currentErrors.isEmpty()));

                allErrors.addAll(currentErrors);

                if (Thread.interrupted()) {
                    throw new InterruptedException("Client is interrupted!");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            eventBus.post(new ImportFinishedEvent(allErrors));

            // update json file with json
            try {
                FileWriter fileWriter = new FileWriter(ratingsHelperPath.toString());
                fileWriter.write(json.toString());
                fileWriter.close();
            }
            catch (IOException e) {
                LOG.error("Updating ratings helper file is failed: ", e);
            }

            persistService.stop();
        }
    }
}
