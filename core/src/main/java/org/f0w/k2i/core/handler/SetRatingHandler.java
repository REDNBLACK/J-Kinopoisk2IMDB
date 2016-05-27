package org.f0w.k2i.core.handler;

import com.google.inject.Inject;
import lombok.val;
import org.f0w.k2i.core.exchange.MovieAuthStringFetcher;
import org.f0w.k2i.core.exchange.MovieRatingChangerFactory;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

public final class SetRatingHandler extends MovieHandler {
    private final MovieAuthStringFetcher authFetcher;
    private final MovieRatingChangerFactory ratingChangerFactory;

    @Inject
    public SetRatingHandler(MovieAuthStringFetcher authFetcher, MovieRatingChangerFactory ratingChangerFactory) {
        this.authFetcher = authFetcher;
        this.ratingChangerFactory = ratingChangerFactory;
    }

    /**
     * Set movie rating and set {@link ImportProgress#rated} to true if succeed
     *
     * @param importProgress Entity to handle
     * @param errors         List which fill with errors if occured
     */
    @Override
    protected void handleMovie(ImportProgress importProgress, List<Error> errors) {
        Movie movie = importProgress.getMovie();

        LOG.info("Setting rating of movie: {}", movie);

        if (importProgress.isRated()) {
            LOG.info("Movie rating is already set!");
            return;
        }

        if (movie.isEmptyRating()) {
            LOG.info("Movie doesn't have rating!");
            return;
        }

        try {
            if (movie.isEmptyIMDBId()) {
                throw new IOException("Can't change movie rating, IMDB ID is not set");
            }

            val authString = authFetcher.prepare(movie).getProcessedResponse();

            if (authString == null) {
                throw new IOException("Movie authorisation string is empty!");
            }

            int statusCode = ratingChangerFactory.create(authString)
                    .prepare(movie)
                    .getProcessedResponse();

            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Can't change movie rating, error status code: " + statusCode);
            }

            importProgress.setRated(true);

            LOG.info("Movie rating was successfully set");
        } catch (IOException e) {
            LOG.error("Error setting rating of movie: {}", e);
            errors.add(new Error(importProgress.getMovie(), e.getMessage()));
        }
    }
}
