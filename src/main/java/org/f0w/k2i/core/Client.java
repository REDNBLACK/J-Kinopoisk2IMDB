package org.f0w.k2i.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.typesafe.config.Config;
import org.f0w.k2i.core.handler.MovieHandler;
import org.f0w.k2i.core.handler.MovieHandlerFactory;
import org.f0w.k2i.core.handler.MovieHandlerType;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;
import org.f0w.k2i.core.model.repository.KinopoiskFileRepository;
import org.f0w.k2i.core.model.repository.MovieRepository;
import org.f0w.k2i.core.providers.ConfigurationProvider;
import org.f0w.k2i.core.providers.JpaRepositoryProvider;
import static org.f0w.k2i.core.utils.FileUtils.*;
import static com.google.common.base.Preconditions.*;
import static org.f0w.k2i.core.utils.exception.LambdaExceptionUtil.rethrowSupplier;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Client {
    private final Injector injector;
    private final File file;

    private MovieHandler movieHandler;
    private KinopoiskFileRepository kinopoiskFileRepository;

    public Client(File file, Config config) {
        this.file = checkNotNull(file);

        injector = Guice.createInjector(
                new ConfigurationProvider(checkNotNull(config)),
                new JpaPersistModule("K2IDB"),
                new JpaRepositoryProvider()
        );
        injector.getInstance(PersistService.class).start();

        movieHandler = injector.getInstance(MovieHandlerFactory.class)
                .make(MovieHandlerType.valueOf(config.getString("mode")));

        kinopoiskFileRepository = injector.getInstance(KinopoiskFileRepository.class);
    }

    public void run() throws IOException {
        String fileHashCode = getHashCode(file);

        KinopoiskFile kinopoiskFile = Optional
                .ofNullable(kinopoiskFileRepository.findByHashCode(fileHashCode))
                .orElseGet(rethrowSupplier(() -> importNewFile(fileHashCode)));

        movieHandler.setKinopoiskFile(kinopoiskFile);
        movieHandler.execute();
    }

    private KinopoiskFile importNewFile(String fileHashCode) throws IOException {
        KinopoiskFile newKinopoiskFile = kinopoiskFileRepository.save(new KinopoiskFile(fileHashCode));

        MovieRepository movieRepository = injector.getInstance(MovieRepository.class);

        List<Movie> movies = parseMovies(file).stream()
                    .map(movieRepository::findOrCreate)
                    .collect(Collectors.toList());

        ImportProgressRepository importProgressRepository = injector.getInstance(ImportProgressRepository.class);
        importProgressRepository.saveAll(newKinopoiskFile, movies);

        return newKinopoiskFile;
    }
}
