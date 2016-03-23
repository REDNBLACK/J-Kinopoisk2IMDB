package org.f0w.k2i.core.handler.command;

import com.google.inject.Inject;
import org.f0w.k2i.core.exchange.MovieWatchlistAssigner;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;

public class AddMovieToWatchlistCommand extends AbstractMovieCommand {
    private MovieWatchlistAssigner assigner;

    @Inject
    public AddMovieToWatchlistCommand(MovieWatchlistAssigner assigner) {
        this.assigner = assigner;
    }

    @Override
    public void execute(ImportProgress importProgress) {
        try {
            Movie movie = importProgress.getMovie();

            if (movie.getImdbId() == null) {
                LOG.info("Can't add movie to watchlist, IMDB ID is not set");
                return;
            }

            LOG.info("Adding movie to watchlist: {}", movie);

            if (importProgress.isImported()) {
                LOG.info("Movie is already added to watchlist!");
                return;
            }

            assigner.sendRequest(movie);

            importProgress.setImported(true);

            LOG.info("Movie was successfully added to watchlist");
        } catch (IOException e) {
            LOG.info("Error adding movie to watchlist: {}", e);
        }
    }
}
