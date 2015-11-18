package org.f0w.k2i.core.exchange.MovieFinders;

import org.f0w.k2i.core.configuration.PropConfiguration;

public class MovieFindersFactory {
    public static MovieFinder make(MovieFinderType movieFinderType) {
        MovieFinder movieFinder;

        switch (movieFinderType) {
            case XML:
                movieFinder = new XMLMovieFinder(new PropConfiguration());
                break;
            case JSON:
                movieFinder = new JSONMovieFinder(new PropConfiguration());
                break;
            case HTML:
                movieFinder = new HTMLMovieFinder(new PropConfiguration());
                break;
            case MIXED:
                movieFinder = new MixedMovieFinder();
                break;
            default:
                throw new IllegalArgumentException("Unexpected MovieFinder type!");
        }

        return movieFinder;
    }
}
