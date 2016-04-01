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
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;
import org.f0w.k2i.core.model.repository.KinopoiskFileRepository;
import org.f0w.k2i.core.model.repository.MovieRepository;
import org.f0w.k2i.core.providers.ConfigurationProvider;
import org.f0w.k2i.core.providers.JpaRepositoryProvider;
import org.f0w.k2i.core.providers.SystemProvider;
import org.f0w.k2i.core.util.exception.KinopoiskToIMDBException;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.f0w.k2i.core.util.FileUtils.*;

public class Client {
    private final File file;
    private final MovieHandler.Type handlerType;
    private final MovieHandler handlerChain;
    private final Provider<MovieRepository> movieRepositoryProvider;
    private final KinopoiskFileRepository kinopoiskFileRepository;
    private final ImportProgressRepository importProgressRepository;
    private final EventBus eventBus;

    public Client(File file, Config config) {
        this.file = requireNonNull(file, "File is not set!");

        Injector injector = Guice.createInjector(
                new ConfigurationProvider(config),
                new JpaRepositoryProvider(),
                new SystemProvider()
        );
        injector.getInstance(PersistService.class).start();

        handlerType = injector.getInstance(MovieHandler.Type.class);
        handlerChain = injector.getInstance(MovieHandler.class);
        kinopoiskFileRepository = injector.getInstance(KinopoiskFileRepository.class);
        importProgressRepository = injector.getInstance(ImportProgressRepository.class);
        movieRepositoryProvider = injector.getProvider(MovieRepository.class);
        eventBus = new EventBus();
    }

    public void registerListeners(List<?> listeners) {
        listeners.forEach(eventBus::register);
    }

    public void registerListener(Object listener) {
        registerListeners(Collections.singletonList(listener));
    }

    public void run() {
        String fileHashCode;

        try {
            fileHashCode = getHashCode(file);
        } catch (IOException e) {
            throw new KinopoiskToIMDBException(e);
        }

        KinopoiskFile kinopoiskFile  = Optional
                    .ofNullable(kinopoiskFileRepository.findByHashCode(fileHashCode))
                    .orElseGet(() -> importNewFile(fileHashCode));

        List<ImportProgress> importProgress = importProgressRepository.findNotImportedOrNotRatedByFile(kinopoiskFile);

        eventBus.post(new ImportStartedEvent(importProgress.size()));

        final List<MovieHandler.Error> allErrors = new ArrayList<>();

        importProgress.forEach(ip -> {
            List<MovieHandler.Error> currentErrors = new ArrayList<>();
            handlerChain.handle(ip, currentErrors, handlerType);

            allErrors.addAll(currentErrors);

            eventBus.post(new ImportProgressAdvancedEvent(currentErrors.isEmpty()));
        });

        eventBus.post(new ImportFinishedEvent(allErrors));
    }

    private KinopoiskFile importNewFile(String fileHashCode) {
        KinopoiskFile newKinopoiskFile = kinopoiskFileRepository.save(new KinopoiskFile(fileHashCode));
        MovieRepository movieRepository = movieRepositoryProvider.get();

        List<Movie> movies;

        try {
            movies = parseMovies(file).stream()
                    .map(movieRepository::findOrCreate)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new KinopoiskToIMDBException(e);
        }

        importProgressRepository.saveAll(newKinopoiskFile, movies);

        return newKinopoiskFile;
    }
}
