package org.f0w.k2i.core.exchange.finder;

import com.google.inject.Inject;
import com.typesafe.config.Config;

import java.util.Arrays;

public class MovieFinderFactory {
    private final Config config;

    @Inject
    private MovieFinderFactory(Config config) {
        this.config = config;
    }

    public MovieFinder make(MovieFinder.Type movieFinderType) {
        switch (movieFinderType) {
            case XML:
                return new XMLMovieFinder(config);
            case JSON:
                return new JSONMovieFinder(config);
            case HTML:
                return new HTMLMovieFinder(config);
            case MIXED:
                return makeMixedMovieFinder();
            default:
                throw new IllegalArgumentException("Invalid movie finder type!");
        }
    }

    private MovieFinder makeMixedMovieFinder() {
        return new MixedMovieFinder(Arrays.asList(
                make(MovieFinder.Type.XML),
                make(MovieFinder.Type.JSON),
                make(MovieFinder.Type.HTML)
        ));
    }
}
