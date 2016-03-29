package org.f0w.k2i.core.command;

import com.google.inject.Inject;
import org.f0w.k2i.core.exchange.MovieWatchlistAssigner;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;
import java.util.Optional;

import static org.f0w.k2i.core.util.MovieFieldsUtils.*;

public class AddToWatchlistCommand extends AbstractMovieCommand {
    private final MovieWatchlistAssigner assigner;

    @Inject
    public AddToWatchlistCommand(MovieWatchlistAssigner assigner) {
        this.assigner = assigner;
    }

    @Override
    public Optional<MovieError> execute(ImportProgress importProgress) {
        Movie movie = importProgress.getMovie();

        LOG.info("Adding movie to watchlist: {}", movie);

        try {
            if (isEmptyIMDBId(movie.getImdbId())) {
                throw new IOException("Can't add movie to watchlist, IMDB ID is not set");
            }

            if (importProgress.isImported()) {
                LOG.info("Movie is already added to watchlist!");
                return Optional.empty();
            }

            assigner.sendRequest(movie);

            importProgress.setImported(true);

            LOG.info("Movie was successfully added to watchlist");

            return Optional.empty();
        } catch (IOException e) {
            LOG.info("Error adding movie to watchlist: {}", e);

            return Optional.of(new MovieError(importProgress.getMovie(), e.getMessage()));
        }
    }
}
