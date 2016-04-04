package org.f0w.k2i.core;

import com.google.common.eventbus.EventBus;
import com.google.inject.*;
import com.google.inject.persist.PersistService;
import com.typesafe.config.Config;
import org.f0w.k2i.core.event.ImportFinishedEvent;
import org.f0w.k2i.core.event.ImportStartedEvent;
import org.f0w.k2i.core.event.ImportProgressAdvancedEvent;
import org.f0w.k2i.core.handler.MovieHandler;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.service.ImportProgressService;
import org.f0w.k2i.core.providers.ConfigurationProvider;
import org.f0w.k2i.core.providers.JpaRepositoryProvider;
import org.f0w.k2i.core.providers.SystemProvider;

import java.io.File;
import java.util.*;

public final class Client {
    private final File file;
    private final MovieHandler.Type handlerType;
    private final MovieHandler handlerChain;
    private final EventBus eventBus;
    private final ImportProgressService importProgressService;

    public Client(File file, Config config) {
        this.file = file;

        Injector injector = Guice.createInjector(
                new ConfigurationProvider(config),
                new JpaRepositoryProvider(),
                new SystemProvider()
        );
        injector.getInstance(PersistService.class).start();

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

    public void run() {
        run(false);
    }

    public void run(boolean cleanRun) {
        List<ImportProgress> importProgress = importProgressService
                .initialize(file, cleanRun)
                .getNotHandledMovies(handlerType);

        eventBus.post(new ImportStartedEvent(importProgress.size()));

        final List<MovieHandler.Error> allErrors = new ArrayList<>();

        importProgress.forEach(ip -> {
            List<MovieHandler.Error> currentErrors = new ArrayList<>();

            handlerChain.handle(ip, currentErrors, handlerType);

            eventBus.post(new ImportProgressAdvancedEvent(currentErrors.isEmpty()));

            allErrors.addAll(currentErrors);
        });

        eventBus.post(new ImportFinishedEvent(allErrors));
    }
}
