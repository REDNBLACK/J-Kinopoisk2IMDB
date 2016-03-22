package org.f0w.k2i.core.exchange.finder;

import java.util.Arrays;
import java.util.List;

public class MovieFindersFactory {
    public static MovieFinder make(MovieFinderType movieFinderType) {
        MovieFinder movieFinder;

        switch (movieFinderType) {
            case XML:
                movieFinder = new XMLMovieFinder();
                break;
            case JSON:
                movieFinder = new JSONMovieFinder();
                break;
            case HTML:
                movieFinder = new HTMLMovieFinder();
                break;
            case MIXED:
                movieFinder = makeMixedMovieFinder();
                break;
            default:
                throw new IllegalArgumentException("Unexpected MovieFinder type!");
        }

        return movieFinder;
    }

    private static MovieFinder makeMixedMovieFinder() {
        List<MovieFinder> movieFinders = Arrays.asList(
                make(MovieFinderType.XML),
                make(MovieFinderType.JSON),
                make(MovieFinderType.HTML)
        );

        return new MixedMovieFinder(movieFinders);
    }
}
