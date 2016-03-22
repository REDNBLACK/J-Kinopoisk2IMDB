package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.exchange.MovieRatingChanger;

import com.google.inject.Inject;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;
import java.util.List;

class SetRatingMovieHandler extends AbstractMovieHandler {
    @Inject
    private MovieRatingChanger changer;

    @Override
    public int execute() {
        List<ImportProgress> importProgress = importProgressRepository.findNotRatedByFileId(kinopoiskFile.getId());

        int successCounter = 0;

        for (ImportProgress progress : importProgress) {
            try {
                successCounter++;

                movieManager.setMovie(progress.getMovie()).prepare();

                if (movieManager.isMoviePrepared()) {
                    Movie preparedMovie = movieManager.getMovie();

                    changer.sendRequest(preparedMovie);

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
