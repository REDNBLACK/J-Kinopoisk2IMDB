package org.f0w.k2i.core;

import ch.qos.logback.classic.Level;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.f0w.k2i.core.command.MovieCommand;
import org.f0w.k2i.core.event.ImportListInitializedEvent;
import org.f0w.k2i.core.event.ImportListProgressAdvancedEvent;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;
import org.f0w.k2i.core.model.repository.KinopoiskFileRepository;
import org.f0w.k2i.core.model.repository.MovieRepository;
import org.f0w.k2i.core.providers.ConfigurationProvider;
import org.f0w.k2i.core.providers.JpaRepositoryProvider;
import org.f0w.k2i.core.providers.SystemProvider;
import org.f0w.k2i.core.util.ConfigValidator;
import org.f0w.k2i.core.util.exception.KinopoiskToIMDBException;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.f0w.k2i.core.util.FileUtils.*;

public class Client {
    private final File file;
    private final MovieCommand movieCommand;
    private final Provider<MovieRepository> movieRepositoryProvider;
    private final KinopoiskFileRepository kinopoiskFileRepository;
    private final ImportProgressRepository importProgressRepository;
    private final EventBus eventBus;

    public Client(File file, Config config) {
        this.file = requireNonNull(file);

        Config configuration = ConfigValidator.checkValid(config.withFallback(ConfigFactory.load()));

        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.toLevel(configuration.getString("log_level")));

        Injector injector = Guice.createInjector(
                new ConfigurationProvider(configuration),
                new JpaPersistModule("K2IDB"),
                new JpaRepositoryProvider(),
                new SystemProvider()
        );
        injector.getInstance(PersistService.class).start();

        movieCommand = injector.getInstance(MovieCommand.class);
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

        KinopoiskFile kinopoiskFile = Optional
                .ofNullable(kinopoiskFileRepository.findByHashCode(fileHashCode))
                .orElseGet(() -> importNewFile(fileHashCode));

        List<ImportProgress> importProgress = importProgressRepository.findNotImportedOrNotRatedByFile(kinopoiskFile);

        eventBus.post(new ImportListInitializedEvent(importProgress.size()));

        importProgress.forEach(ip -> {
            movieCommand.execute(ip);

            eventBus.post(new ImportListProgressAdvancedEvent());
        });
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
