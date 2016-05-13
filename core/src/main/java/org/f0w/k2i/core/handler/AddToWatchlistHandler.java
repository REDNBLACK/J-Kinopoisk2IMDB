package org.f0w.k2i.core.handler;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.f0w.k2i.core.exchange.MovieWatchlistAssigner;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

public final class AddToWatchlistHandler extends MovieHandler {
    private final Config config;

    @Inject
    public AddToWatchlistHandler(Config config) {
        this.config = config;
    }

    /**
     * Add Movie to a watchlist and set {@link ImportProgress#imported} to true if succeed
     *
     * @param importProgress Entity to handle
     * @param errors         List which fill with errors if occured
     */
    @Override
    protected void handleMovie(ImportProgress importProgress, List<Error> errors) {
        Movie movie = importProgress.getMovie();

        LOG.info("Adding movie to watchlist: {}", movie);

        try {
            if (movie.isEmptyIMDBId()) {
                throw new IOException("Can't add movie to watchlist, IMDB ID is not set");
            }

            if (importProgress.isImported()) {
                LOG.info("Movie is already added to watchlist!");
                return;
            }
            
            int statusCode = new MovieWatchlistAssigner(config)
                    .prepare(movie)
                    .getProcessedResponse();

            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Can't add movie to watchlist, error status code: " + statusCode);
            }

            importProgress.setImported(true);

            LOG.info("Movie was successfully added to watchlist");
        } catch (IOException e) {
            LOG.info("Error adding movie to watchlist: {}", e);

            errors.add(new Error(importProgress.getMovie(), e.getMessage()));
        }
    }
}
