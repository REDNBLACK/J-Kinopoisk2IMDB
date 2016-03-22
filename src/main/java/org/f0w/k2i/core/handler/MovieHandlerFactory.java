package org.f0w.k2i.core.handler;

public class MovieHandlerFactory {
    public static MovieHandler make(MovieHandlerType movieHandlerType) {
        MovieHandler handler;

        switch (movieHandlerType) {
            case ADD_TO_WATCHLIST:
                handler = new AddToWatchListMovieHandler();
                break;
            case SET_RATING:
                handler = new SetRatingMovieHandler();
                break;
            case EVERYTHING:
                handler = new DoEverythingMovieHandler();
                break;
            default:
                throw new IllegalArgumentException("Unexpected comparator type!");
        }

        return handler;
    }
}
