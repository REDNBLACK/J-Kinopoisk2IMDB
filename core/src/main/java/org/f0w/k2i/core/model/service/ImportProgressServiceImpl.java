package org.f0w.k2i.core.model.service;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.typesafe.config.Config;
import org.f0w.k2i.core.handler.MovieHandler;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class ImportProgressServiceImpl implements ImportProgressService {
    private final KinopoiskFileServiceImpl kinopoiskFileService;
    private final MovieServiceImpl movieService;

    private final ImportProgressRepository importProgressRepository;
    private final MovieHandler.Type movieHandlerType;

    @Inject
    public ImportProgressServiceImpl(
            KinopoiskFileServiceImpl kinopoiskFileService,
            MovieServiceImpl movieService,
            ImportProgressRepository importProgressRepository,
            MovieHandler.Type movieHandlerType
    ) {
        this.kinopoiskFileService = kinopoiskFileService;
        this.movieService = movieService;
        this.importProgressRepository = importProgressRepository;
        this.movieHandlerType = movieHandlerType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ImportProgress> findAll(Path filePath, Config config) {
        KinopoiskFile kinopoiskFile = kinopoiskFileService.find(filePath);

        if (kinopoiskFile == null) {
            return Collections.emptyList();
        }

        String listId = config.getString("list");

        switch (movieHandlerType) {
            case SET_RATING:
                return importProgressRepository.findNotRatedByFile(kinopoiskFile, listId);
            case ADD_TO_WATCHLIST:
                return importProgressRepository.findNotImportedByFile(kinopoiskFile, listId);
            case COMBINED:
                return importProgressRepository.findNotImportedOrNotRatedByFile(kinopoiskFile, listId);
            default:
                return Collections.emptyList();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void saveAll(Path filePath) {
        KinopoiskFile newFile = kinopoiskFileService.save(filePath);
        List<Movie> movies = movieService.saveOrUpdateAll(filePath);

        importProgressRepository.saveAll(newFile, movies, config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ImportProgress save(ImportProgress importProgress) {
        return importProgressRepository.save(importProgress);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteAll(Path filePath) {
        KinopoiskFile existingFile = kinopoiskFileService.find(filePath);

        if (existingFile == null) {
            return;
        }

        kinopoiskFileService.delete(filePath);
        importProgressRepository.deleteAll(existingFile);
    }
}
