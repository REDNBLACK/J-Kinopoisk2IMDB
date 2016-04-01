package org.f0w.k2i.core.handler;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import org.f0w.k2i.core.exchange.MovieWatchlistAssigner;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.f0w.k2i.core.util.MovieFieldsUtils.isEmptyIMDBId;

public class AddToWatchlistHandler extends AbstractMovieHandler implements MovieHandler {
    private final MovieWatchlistAssigner assigner;

    @Inject
    public AddToWatchlistHandler(MovieWatchlistAssigner assigner) {
        this.assigner = assigner;
        this.types = ImmutableSet.of(Type.ADD_TO_WATCHLIST, Type.COMBINED);
    }

    @Override
    protected void handleMovie(ImportProgress importProgress, List<Error> errors) {
        Movie movie = importProgress.getMovie();

        LOG.info("Adding movie to watchlist: {}", movie);

        try {
            if (isEmptyIMDBId(movie.getImdbId())) {
                throw new IOException("Can't add movie to watchlist, IMDB ID is not set");
            }

            if (importProgress.isImported()) {
                LOG.info("Movie is already added to watchlist!");
                return;
            }

            assigner.sendRequest(movie);

            importProgress.setImported(true);

            LOG.info("Movie was successfully added to watchlist");
        } catch (IOException e) {
            LOG.info("Error adding movie to watchlist: {}", e);

            errors.add(new Error(importProgress, e.getMessage()));
        }
    }
}
