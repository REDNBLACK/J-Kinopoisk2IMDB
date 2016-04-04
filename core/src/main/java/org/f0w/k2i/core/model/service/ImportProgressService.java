package org.f0w.k2i.core.model.service;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
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

import static org.f0w.k2i.core.util.FileUtils.*;

public final class ImportProgressService {
    private final KinopoiskFileRepository kinopoiskFileRepository;
    private final ImportProgressRepository importProgressRepository;
    private final MovieRepository movieRepository;

    private File file;
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

    public ImportProgressService initialize(File file, boolean cleanRun) {
        this.file = checkFile(file);

        String fileHashCode = getHashCode(file);

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
        List<Movie> movies = movieRepository.saveAllNotExisting(parseMovies(file));

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
