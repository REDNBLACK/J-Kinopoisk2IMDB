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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.f0w.k2i.core.util.IOUtils.checkFile;

public final class Client implements Runnable {
    private final File file;
    private final MovieHandler.Type handlerType;
    private final MovieHandler handlerChain;
    private final EventBus eventBus;
    private final ImportProgressService importProgressService;
    private final PersistService persistService;
    private final boolean cleanRun;

    public Client(File file, Config config) {
        this(file, config, false);
    }

    public Client(File file, Config config, boolean cleanRun) {
        this.file = checkFile(file);
        this.cleanRun = cleanRun;

        Injector injector = Guice.createInjector(
                new ConfigurationProvider(config),
                new JpaRepositoryProvider(),
                new SystemProvider()
        );

        persistService = injector.getInstance(PersistService.class);
        persistService.start();

        handlerType = injector.getInstance(MovieHandler.Type.class);
        handlerChain = injector.getInstance(MovieHandler.class);
        eventBus = new EventBus();
        importProgressService = injector.getInstance(ImportProgressService.class);
    }

    public void registerListeners(List<?> listeners) {
        listeners.forEach(eventBus::register);
    }

    public void registerListener(Object listener) {
        registerListeners(Collections.singletonList(listener));
    }

    @Override
    public void run() {
        List<ImportProgress> importProgress = importProgressService
                .initialize(file, cleanRun)
                .getNotHandledMovies(handlerType);

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
