package org.f0w.k2i.core.exchange.finder;

import com.google.inject.Inject;
import com.typesafe.config.Config;

import java.util.Arrays;
import java.util.List;

public class MovieFindersFactory {
    private final Config config;

    @Inject
    public MovieFindersFactory(Config config) {
        this.config = config;
    }

    public MovieFinder make(MovieFinderType movieFinderType) {
        MovieFinder movieFinder;

        switch (movieFinderType) {
            case XML:
                movieFinder = new XMLMovieFinder(config);
                break;
            case JSON:
                movieFinder = new JSONMovieFinder(config);
                break;
            case HTML:
                movieFinder = new HTMLMovieFinder(config);
                break;
            case MIXED:
                movieFinder = makeMixedMovieFinder();
                break;
            default:
                throw new IllegalArgumentException("Unexpected MovieFinder type!");
        }

        return movieFinder;
    }

    private MovieFinder makeMixedMovieFinder() {
        List<MovieFinder> movieFinders = Arrays.asList(
                make(MovieFinderType.XML),
                make(MovieFinderType.JSON),
                make(MovieFinderType.HTML)
        );

        return new MixedMovieFinder(movieFinders);
    }
}
