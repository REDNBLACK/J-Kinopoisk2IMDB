package org.f0w.k2i.core.handler;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import lombok.val;
import org.f0w.k2i.core.exchange.MovieAuthStringFetcher;
import org.f0w.k2i.core.exchange.MovieRatingChanger;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import static org.f0w.k2i.core.util.MovieUtils.isEmptyIMDBId;
import static org.f0w.k2i.core.util.MovieUtils.isEmptyRating;

public final class SetRatingHandler extends MovieHandler {
    private final Config config;

    @Inject
    public SetRatingHandler(Config config) {
        this.config = config;
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

            val authString = new MovieAuthStringFetcher(config)
                    .prepare(movie)
                    .getProcessedResponse();

            if (authString == null) {
                throw new IOException("Movie authorisation string is empty!");
            }

            int statusCode = new MovieRatingChanger(config, authString)
                    .prepare(movie)
                    .getProcessedResponse();

            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Can't change movie rating, error status code: " + statusCode);
            }

            importProgress.setRated(true);

            LOG.info("Movie rating was successfully set");
        } catch (IOException e) {
            LOG.info("Error setting rating of movie: {}", e);

            errors.add(new Error(importProgress.getMovie(), e.getMessage()));
        }
    }
}
