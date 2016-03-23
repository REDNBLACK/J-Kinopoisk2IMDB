package org.f0w.k2i.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.f0w.k2i.core.handler.MovieHandler;
import org.f0w.k2i.core.handler.MovieHandlerFactory;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;
import org.f0w.k2i.core.model.repository.KinopoiskFileRepository;
import org.f0w.k2i.core.model.repository.MovieRepository;
import org.f0w.k2i.core.providers.ConfigurationProvider;
import org.f0w.k2i.core.providers.JpaRepositoryProvider;
import org.f0w.k2i.core.utils.ConfigValidator;
import org.f0w.k2i.core.utils.exception.KinopoiskToIMDBException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.f0w.k2i.core.utils.FileUtils.*;

public class Client extends Observable {
    private final Injector injector;
    private final File file;

    private MovieHandler movieHandler;

    private Provider<MovieRepository> movieRepositoryProvider;
    private KinopoiskFileRepository kinopoiskFileRepository;
    private ImportProgressRepository importProgressRepository;

    public Client(File file, Config config) {
        this.file = requireNonNull(file);

        Config configuration = ConfigValidator.checkValid(config.withFallback(ConfigFactory.load()));

        injector = Guice.createInjector(
                new ConfigurationProvider(configuration),
                new JpaPersistModule("K2IDB"),
                new JpaRepositoryProvider()
        );
        injector.getInstance(PersistService.class).start();

        movieHandler = injector.getInstance(MovieHandlerFactory.class)
                .make(MovieHandler.Type.valueOf(configuration.getString("mode")));

        kinopoiskFileRepository = injector.getInstance(KinopoiskFileRepository.class);
        importProgressRepository = injector.getInstance(ImportProgressRepository.class);
        movieRepositoryProvider = injector.getProvider(MovieRepository.class);
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

        importProgress.forEach(ip -> {
            setChanged();
            notifyObservers();

            movieHandler.execute(ip);

            importProgressRepository.save(ip);
        });
    }

    private KinopoiskFile importNewFile(String fileHashCode) {
        KinopoiskFile newKinopoiskFile = kinopoiskFileRepository.save(new KinopoiskFile(fileHashCode));

        List<Movie> movies;

        try {
            movies = parseMovies(file).stream()
                    .map(movieRepositoryProvider.get()::findOrCreate)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new KinopoiskToIMDBException(e);
        }

        importProgressRepository.saveAll(newKinopoiskFile, movies);

        return newKinopoiskFile;
    }
}
