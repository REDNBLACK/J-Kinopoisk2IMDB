package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.MovieManager;
import org.f0w.k2i.core.exchange.MovieRatingChanger;

import com.google.inject.Inject;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

class SetRatingMovieHandler extends AbstractMovieHandler {
    private MovieRatingChanger changer;

    @Inject
    public SetRatingMovieHandler(MovieManager movieManager, MovieRatingChanger changer) {
        super(movieManager);
        this.changer = changer;
    }

    @Override
    public void execute(List<ImportProgress> importProgress, Consumer<ImportProgress> consumer) {
        for (ImportProgress progress : importProgress) {
            try {
                LOG.info("Setting rating of movie: {}", progress.getMovie());

                if (progress.isRated()) {
                    LOG.info("Movie rating is already set!");
                    continue;
                }

                Movie preparedMovie = movieManager.setMovie(progress.getMovie())
                        .prepare()
                        .getMovie();

                changer.sendRequest(preparedMovie);

                progress.setRated(true);
                progress.setMovie(preparedMovie);

                consumer.accept(progress);

                LOG.info("Movie rating was successfully set");
            } catch (IOException e) {
                LOG.info("Error setting rating of movie: {}", e);
            }
        }
    }
}
