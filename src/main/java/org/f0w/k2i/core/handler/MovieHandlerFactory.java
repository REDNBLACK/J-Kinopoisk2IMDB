package org.f0w.k2i.core.handler;

import java.util.Arrays;
import java.util.List;

public class MovieHandlerFactory {
    private MovieHandlerFactory() {
        throw new UnsupportedOperationException();
    }

    public static MovieHandler make(MovieHandlerType movieHandlerType) {
        MovieHandler handler;

        switch (movieHandlerType) {
            case ADD_TO_WATCHLIST:
                handler = new AddToWatchListMovieHandler();
                break;
            case SET_RATING:
                handler = new SetRatingMovieHandler();
                break;
            case COMBINED:
                handler = makeCombinedHandler();
                break;
            default:
                throw new IllegalArgumentException("Unexpected comparator type!");
        }

        return handler;
    }

    private static MovieHandler makeCombinedHandler() {
        List<MovieHandler> handlers = Arrays.asList(
            make(MovieHandlerType.ADD_TO_WATCHLIST),
            make(MovieHandlerType.SET_RATING)
        );

        return new CombinedMovieHandler(handlers);
    }
}
