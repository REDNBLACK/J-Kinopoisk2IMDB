package org.f0w.k2i.core.model.service;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import org.f0w.k2i.core.handler.MovieHandler;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;
import org.f0w.k2i.core.model.repository.KinopoiskFileRepository;
import org.f0w.k2i.core.model.repository.MovieRepository;
import org.f0w.k2i.core.util.MovieUtils;
import org.f0w.k2i.core.util.exception.ExceptionUtils;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.f0w.k2i.core.util.IOUtils.checkFile;

public final class ImportProgressService {
    private final KinopoiskFileRepository kinopoiskFileRepository;
    private final ImportProgressRepository importProgressRepository;
    private final MovieRepository movieRepository;

    private Path filePath;
    private KinopoiskFile kinopoiskFile;

    @Inject
    public ImportProgressService(
            KinopoiskFileRepository kinopoiskFileRepository,
            ImportProgressRepository importProgressRepository,
            MovieRepository movieRepository
    ) {
        this.kinopoiskFileRepository = kinopoiskFileRepository;
        this.importProgressRepository = importProgressRepository;
        this.movieRepository = movieRepository;
    }

    public ImportProgressService initialize(Path filePath, boolean cleanRun) {
        this.filePath = checkFile(filePath);

        String fileHashCode = ExceptionUtils.uncheck(() -> Files.hash(filePath.toFile(), Hashing.sha256()).toString());

        if (cleanRun) {
            removeExistingFileData(fileHashCode);
        }

        kinopoiskFile = Optional.ofNullable(kinopoiskFileRepository.findByHashCode(fileHashCode))
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

    @Transactional
    private KinopoiskFile importNewFileData(String fileHashCode) {
        KinopoiskFile newFile = kinopoiskFileRepository.save(new KinopoiskFile(fileHashCode));

        List<Movie> movies = MovieUtils.parseMovies(filePath)
                .stream()
                .map(newMovie -> {
                    Movie oldMovie = movieRepository.findByTitleAndYear(newMovie.getTitle(), newMovie.getYear());

                    if (oldMovie == null) {
                        return movieRepository.save(newMovie);
                    }

                    if (oldMovie.getRating() == null && newMovie.getRating() != null) {
                        oldMovie.setRating(newMovie.getRating());

                        return movieRepository.save(oldMovie);
                    }

                    return oldMovie;
                })
                .collect(Collectors.toList());

        importProgressRepository.saveAll(newFile, movies);

        return newFile;
    }

    @Transactional
    private void removeExistingFileData(String fileHashCode) {
        KinopoiskFile existingFile = kinopoiskFileRepository.findByHashCode(fileHashCode);

        if (existingFile == null) {
            return;
        }

        importProgressRepository.deleteAll(existingFile);
        kinopoiskFileRepository.delete(existingFile);
    }
}
