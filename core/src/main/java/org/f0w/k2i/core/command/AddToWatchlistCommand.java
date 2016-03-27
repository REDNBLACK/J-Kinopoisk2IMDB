package org.f0w.k2i.core.command;

import com.google.inject.Inject;
import org.f0w.k2i.core.exchange.MovieWatchlistAssigner;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;

import static org.f0w.k2i.core.util.MovieFieldsUtils.*;

class AddToWatchlistCommand extends AbstractMovieCommand {
    private final MovieWatchlistAssigner assigner;

    @Inject
    public AddToWatchlistCommand(MovieWatchlistAssigner assigner) {
        this.assigner = assigner;
    }

    @Override
    public void execute(ImportProgress importProgress) {
        try {
            Movie movie = importProgress.getMovie();

            if (isEmptyIMDBId(movie.getImdbId())) {
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
