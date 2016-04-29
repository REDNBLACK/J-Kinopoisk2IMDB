package org.f0w.k2i.core;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.typesafe.config.Config;
import org.f0w.k2i.core.event.ImportFinishedEvent;
import org.f0w.k2i.core.event.ImportProgressAdvancedEvent;
import org.f0w.k2i.core.event.ImportStartedEvent;
import org.f0w.k2i.core.handler.MovieHandler;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.service.ImportProgressService;
import org.f0w.k2i.core.providers.ConfigurationProvider;
import org.f0w.k2i.core.providers.JpaRepositoryProvider;
import org.f0w.k2i.core.providers.SystemProvider;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Main facade implemented as {@link Runnable} for working with core module.
 */
public final class Client implements Runnable {
    private final MovieHandler.Type handlerType;
    private final MovieHandler handlerChain;
    private final EventBus eventBus;
    private final ImportProgressService importProgressService;
    private final PersistService persistService;

    public Client(Path filePath, Config config) {
        this(filePath, config, false);
    }

    public Client(Path filePath, Config config, boolean cleanRun) {
        this(filePath, config, cleanRun, Collections.emptyList());
    }

    public Client(Path filePath, Config config, boolean cleanRun, List<?> listeners) {
        Injector injector = Guice.createInjector(
                new ConfigurationProvider(config),
                new JpaRepositoryProvider(),
                new SystemProvider()
        );

        persistService = injector.getInstance(PersistService.class);
        persistService.start();

        handlerType = injector.getInstance(MovieHandler.Type.class);
        handlerChain = injector.getInstance(MovieHandler.class);
        importProgressService = injector.getInstance(ImportProgressService.class)
                .initialize(filePath, cleanRun);

        eventBus = new EventBus();
        requireNonNull(listeners).forEach(eventBus::register);
    }

    @Override
    public void run() {
        List<ImportProgress> importProgress = importProgressService.getNotHandledMovies(handlerType);

        eventBus.post(new ImportStartedEvent(importProgress.size()));

        final List<MovieHandler.Error> allErrors = new ArrayList<>();

        try {
            for (ImportProgress current : importProgress) {
                List<MovieHandler.Error> currentErrors = new ArrayList<>();

                handlerChain.handle(current, currentErrors, handlerType);

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

            persistService.stop();
        }
    }
}
