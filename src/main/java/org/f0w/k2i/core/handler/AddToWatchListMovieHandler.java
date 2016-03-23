package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.MovieManager;
import org.f0w.k2i.core.exchange.MovieWatchlistAssigner;

import com.google.inject.Inject;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

class AddToWatchListMovieHandler extends AbstractMovieHandler {
    private MovieWatchlistAssigner assigner;

    @Inject
    public AddToWatchListMovieHandler(MovieManager movieManager, MovieWatchlistAssigner assigner) {
        super(movieManager);
        this.assigner = assigner;
    }

    @Override
    public void execute(
            List<ImportProgress> importProgress,
            Consumer<ImportProgress> onSuccess,
            Consumer<ImportProgress> everyTime
    ) {
        for (ImportProgress progress : importProgress) {
            try {
                LOG.info("Adding movie to watchlist: {}", progress.getMovie());

                if (progress.isImported()) {
                    LOG.info("Movie is already added to watchlist!");
                    continue;
                }

                Movie preparedMovie =  movieManager.setMovie(progress.getMovie())
                        .prepare()
                        .getMovie();

                assigner.sendRequest(preparedMovie);

                progress.setImported(true);
                progress.setMovie(preparedMovie);

                onSuccess.accept(progress);

                LOG.info("Movie was successfully added to watchlist");
            } catch (IOException e) {
                LOG.info("Error adding movie to watchlist: {}", e);
            } finally {
                everyTime.accept(progress);
            }
        }
    }
}
