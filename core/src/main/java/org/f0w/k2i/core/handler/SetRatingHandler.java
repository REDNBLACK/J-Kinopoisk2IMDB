package org.f0w.k2i.core.handler;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import org.f0w.k2i.core.exchange.MovieRatingChanger;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import static org.f0w.k2i.core.util.MovieUtils.isEmptyIMDBId;
import static org.f0w.k2i.core.util.MovieUtils.isEmptyRating;

public final class SetRatingHandler extends AbstractMovieHandler implements MovieHandler {
    private final MovieRatingChanger changer;

    @Inject
    public SetRatingHandler(MovieRatingChanger changer) {
        this.changer = changer;
        this.types = ImmutableSet.of(Type.SET_RATING, Type.COMBINED);
    }

    @Override
    protected void handleMovie(ImportProgress importProgress, List<Error> errors) {
        Movie movie = importProgress.getMovie();

        LOG.info("Setting rating of movie: {}", movie);

        try {
            if (isEmptyIMDBId(movie.getImdbId())) {
                throw new IOException("Can't change movie rating, IMDB ID is not set");
            }

            if (isEmptyRating(movie.getRating())) {
                LOG.info("Movie doesn't have rating!");
                return;
            }

            if (importProgress.isRated()) {
                LOG.info("Movie rating is already set!");
                return;
            }

            changer.sendRequest(movie);

            Long statusCode = changer.getProcessedResponse();

            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Can't change movie rating, error status code: " + statusCode);
            }

            importProgress.setRated(true);

            LOG.info("Movie rating was successfully set");
        } catch (IOException e) {
            LOG.info("Error setting rating of movie: {}", e);

            errors.add(new Error(importProgress, e.getMessage()));
        }
    }
}
