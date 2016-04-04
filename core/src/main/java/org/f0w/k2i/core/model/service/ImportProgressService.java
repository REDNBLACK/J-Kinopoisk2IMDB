package org.f0w.k2i.core.model.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.f0w.k2i.core.handler.MovieHandler;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;
import org.f0w.k2i.core.model.repository.KinopoiskFileRepository;
import org.f0w.k2i.core.model.repository.MovieRepository;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.f0w.k2i.core.util.FileUtils.*;

public final class ImportProgressService {
    private final KinopoiskFileRepository kinopoiskFileRepository;
    private final ImportProgressRepository importProgressRepository;
    private final Provider<MovieRepository> movieRepositoryProvider;

    private File file;
    private KinopoiskFile kinopoiskFile;

    @Inject
    public ImportProgressService(
            KinopoiskFileRepository kinopoiskFileRepository,
            ImportProgressRepository importProgressRepository,
            Provider<MovieRepository> movieRepositoryProvider
    ) {
        this.kinopoiskFileRepository = kinopoiskFileRepository;
        this.importProgressRepository = importProgressRepository;
        this.movieRepositoryProvider = movieRepositoryProvider;
    }

    public ImportProgressService initialize(File file, boolean cleanRun) {
        this.file = checkFile(file);

        String fileHashCode = getHashCode(file);

        if (cleanRun) {
            removeExistingFileData(fileHashCode);
        }

        kinopoiskFile  = Optional
                .ofNullable(kinopoiskFileRepository.findByHashCode(fileHashCode))
                .orElseGet(() -> importNewFileData(fileHashCode));

        return this;
    }

    public List<ImportProgress> getNotHandledMovies(MovieHandler.Type type) {
        switch (type) {
            case SET_RATING:
                return importProgressRepository.findNotRatedByFile(kinopoiskFile);
            case ADD_TO_WATCHLIST:
                return importProgressRepository.findNotImportedByFile(kinopoiskFile);
            case COMBINED:
                return importProgressRepository.findNotImportedOrNotRatedByFile(kinopoiskFile);
            default:
                return Collections.emptyList();
        }
    }

    private KinopoiskFile importNewFileData(String fileHashCode) {
        KinopoiskFile newFile = kinopoiskFileRepository.save(new KinopoiskFile(fileHashCode));
        MovieRepository movieRepository = movieRepositoryProvider.get();

        List<Movie> movies = parseMovies(file).stream()
                .map(movieRepository::findOrCreate)
                .collect(Collectors.toList());

        importProgressRepository.saveAll(newFile, movies);

        return newFile;
    }

    private void removeExistingFileData(String fileHashCode) {
        KinopoiskFile existingFile = kinopoiskFileRepository.findByHashCode(fileHashCode);

        if (existingFile == null) {
            return;
        }

        importProgressRepository.deleteAll(existingFile);
        kinopoiskFileRepository.delete(existingFile);
    }
}
