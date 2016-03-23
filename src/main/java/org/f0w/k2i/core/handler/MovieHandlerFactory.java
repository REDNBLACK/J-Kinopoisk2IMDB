package org.f0w.k2i.core.handler;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.f0w.k2i.core.MovieManager;
import org.f0w.k2i.core.exchange.MovieRatingChanger;
import org.f0w.k2i.core.exchange.MovieWatchlistAssigner;

import java.util.Arrays;
import java.util.List;

public class MovieHandlerFactory {
    private final Provider<MovieManager> movieManagerProvider;
    private final Provider<MovieWatchlistAssigner> movieWatchlistAssignerProvider;
    private final Provider<MovieRatingChanger> movieRatingChangerProvider;

    @Inject
    public MovieHandlerFactory(
            Provider<MovieManager> movieManagerProvider,
            Provider<MovieWatchlistAssigner> movieWatchlistAssignerProvider,
            Provider<MovieRatingChanger> movieRatingChangerProvider
    ) {
        this.movieManagerProvider = movieManagerProvider;
        this.movieWatchlistAssignerProvider = movieWatchlistAssignerProvider;
        this.movieRatingChangerProvider = movieRatingChangerProvider;
    }

    public MovieHandler make(MovieHandlerType movieHandlerType) {
        switch (movieHandlerType) {
            case ADD_TO_WATCHLIST:
                return new AddToWatchListMovieHandler(movieManagerProvider.get(), movieWatchlistAssignerProvider.get());
            case SET_RATING:
                return new SetRatingMovieHandler(movieManagerProvider.get(), movieRatingChangerProvider.get());
            case COMBINED:
                return makeCombinedHandler();
            default:
                throw new IllegalArgumentException("Unexpected comparator type!");
        }
    }

    private MovieHandler makeCombinedHandler() {
        List<MovieHandler> handlers = Arrays.asList(
            make(MovieHandlerType.ADD_TO_WATCHLIST),
            make(MovieHandlerType.SET_RATING)
        );

        return new CombinedMovieHandler(handlers);
    }
}
