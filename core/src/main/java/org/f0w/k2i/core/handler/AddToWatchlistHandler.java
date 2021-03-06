package org.f0w.k2i.core.handler;

import com.google.inject.Inject;
import org.f0w.k2i.core.exchange.MovieWatchlistAssigner;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.HttpStatusException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

public final class AddToWatchlistHandler extends MovieHandler {
    private final MovieWatchlistAssigner assigner;

    @Inject
    public AddToWatchlistHandler(MovieWatchlistAssigner assigner) {
        this.assigner = assigner;
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

        // This is never shown. To make it work we should not filter imported=false in
        // findNotImportedByFile method

//        if (importProgress.isImported()) {
//            LOG.info("Movie is already added to watchlist!");
//            return;
//        }

        try {
            if (movie.isEmptyIMDBId()) {
                throw new IOException("Can't add movie to watchlist, IMDB ID is not set");
            }

            int statusCode;

            try {
                statusCode = assigner.prepare(movie).getProcessedResponse();
            }
            catch(HttpStatusException e) {
                statusCode = e.getStatusCode();
            }

            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Can't add movie to watchlist, error status code: " + statusCode);
            }

            importProgress.setImported(true);

            LOG.info("Movie was successfully added to watchlist");
        } catch (IOException e) {
            LOG.error("Error adding movie to watchlist: ", e);
            errors.add(new Error(importProgress.getMovie(), e.getMessage()));
        }
    }
}
