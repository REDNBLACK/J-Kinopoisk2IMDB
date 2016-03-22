package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.exchange.MovieWatchlistAssigner;

import com.google.inject.Inject;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;
import java.util.List;

class AddToWatchListMovieHandler extends AbstractMovieHandler {
    @Inject
    private MovieWatchlistAssigner assigner;

    @Override
    public int execute() {
        List<ImportProgress> importProgress = importProgressRepository.findNotImportedByFileId(kinopoiskFile.getId());

        int successCounter = 0;

        for (ImportProgress progress : importProgress) {
            try {
                successCounter++;

                movieManager.setMovie(progress.getMovie()).prepare();

                if (movieManager.isMoviePrepared()) {
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
