package org.f0w.k2i.core.exchange.MovieFinders;

import org.f0w.k2i.core.Components.Configuration;
import org.f0w.k2i.core.net.HttpRequest;

public class MovieFindersFactory {
    public static MovieFinder make(MovieFinderType movieFinderType) {
        MovieFinder movieFinder;

        switch (movieFinderType) {
            case XML:
                movieFinder = new XMLMovieFinder(new HttpRequest(), new Configuration());
                break;
            case JSON:
                movieFinder = new JSONMovieFinder(new HttpRequest(), new Configuration());
                break;
            case HTML:
                movieFinder = new HTMLMovieFinder(new HttpRequest(), new Configuration());
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
