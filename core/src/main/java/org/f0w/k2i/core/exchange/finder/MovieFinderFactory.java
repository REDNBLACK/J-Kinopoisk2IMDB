package org.f0w.k2i.core.exchange.finder;

import com.google.inject.Inject;
import com.typesafe.config.Config;

import java.util.Arrays;

/**
 * {@link MovieFinder} factory
 */
public class MovieFinderFactory {
    private final Config config;

    @Inject
    private MovieFinderFactory(Config config) {
        this.config = config;
    }

    /**
     * Create instance of {@link MovieFinder},
     * using {@link MovieFinder.Type} as argument.
     * @param type Type of MovieComparator
     * @return MovieFinder instance
     */
    public MovieFinder make(MovieFinder.Type type) {
        switch (type) {
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

    /**
     * Create instance of {@link MixedMovieFinder}
     * @return MixedMovieFinder
     */
    private MovieFinder makeMixedMovieFinder() {
        return new MixedMovieFinder(Arrays.asList(
                make(MovieFinder.Type.XML),
                make(MovieFinder.Type.JSON),
                make(MovieFinder.Type.HTML)
        ));
    }
}
