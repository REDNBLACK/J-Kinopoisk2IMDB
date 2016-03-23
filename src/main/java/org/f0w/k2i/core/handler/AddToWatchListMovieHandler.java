package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.MovieManager;
import org.f0w.k2i.core.exchange.MovieWatchlistAssigner;

import com.google.inject.Inject;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;

import java.io.IOException;
import java.util.List;

class AddToWatchListMovieHandler extends AbstractMovieHandler {
    private MovieWatchlistAssigner assigner;

    @Inject
    public AddToWatchListMovieHandler(
            ImportProgressRepository importProgressRepository,
            MovieManager movieManager,
            MovieWatchlistAssigner assigner
    ) {
        super(importProgressRepository, movieManager);
        this.assigner = assigner;
    }

    @Override
    public int execute() {
        List<ImportProgress> importProgress = importProgressRepository.findNotImportedByFile(kinopoiskFile);

        int successCounter = 0;

        for (ImportProgress progress : importProgress) {
            try {
                successCounter++;

                movieManager.setMovie(progress.getMovie()).prepare();

                if (movieManager.isPrepared()) {
                    Movie preparedMovie = movieManager.getMovie();

                    assigner.sendRequest(preparedMovie);

                    progress.setImported(true);
                    progress.setMovie(preparedMovie);

                    importProgressRepository.save(progress);
                }
            } catch (IOException e) {
                successCounter--;
            }
        }

        return successCounter;
    }
}
