package org.f0w.k2i.core.command;

import com.google.inject.Inject;
import org.f0w.k2i.core.exchange.MovieRatingChanger;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;
import java.util.Optional;

import static org.f0w.k2i.core.util.MovieFieldsUtils.*;

public class SetRatingCommand extends AbstractMovieCommand {
    private final MovieRatingChanger changer;

    @Inject
    public SetRatingCommand(MovieRatingChanger changer) {
        this.changer = changer;
    }

    @Override
    public Optional<MovieError> execute(ImportProgress importProgress) {
        Movie movie = importProgress.getMovie();

        LOG.info("Setting rating of movie: {}", movie);

        try {
            if (isEmptyIMDBId(movie.getImdbId())) {
                throw new IOException("Can't change movie rating, IMDB ID is not set");
            }

            if (isEmptyRating(movie.getRating())) {
                LOG.info("Movie doesn't have rating!");

                return Optional.empty();
            }

            if (importProgress.isRated()) {
                LOG.info("Movie rating is already set!");

                return Optional.empty();
            }

            changer.sendRequest(movie);

            importProgress.setRated(true);

            LOG.info("Movie rating was successfully set");

            return Optional.empty();
        } catch (IOException e) {
            LOG.info("Error setting rating of movie: {}", e);

            return Optional.of(new MovieError(importProgress.getMovie(), e.getMessage()));
        }
    }
}
