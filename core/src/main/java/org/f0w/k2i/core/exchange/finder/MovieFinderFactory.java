package org.f0w.k2i.core.exchange.finder;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.f0w.k2i.core.exchange.finder.strategy.HTMLExchangeStrategy;
import org.f0w.k2i.core.exchange.finder.strategy.JSONExchangeStrategy;
import org.f0w.k2i.core.exchange.finder.strategy.XMLExchangeStrategy;

import java.util.Arrays;

import static org.f0w.k2i.core.exchange.finder.MovieFinder.Type.*;

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
     *
     * @param type Type of MovieComparator
     * @return MovieFinder instance
     */
    public MovieFinder make(MovieFinder.Type type) {
        switch (type) {
            case XML:
                return new BasicMovieFinder(XML, new XMLExchangeStrategy(), config);
            case JSON:
                return new BasicMovieFinder(JSON, new JSONExchangeStrategy(), config);
            case HTML:
                return new BasicMovieFinder(HTML, new HTMLExchangeStrategy(), config);
            case MIXED:
                return makeMixedMovieFinder();
            default:
                throw new IllegalArgumentException("Invalid movie finder type!");
        }
    }

    /**
     * Create instance of {@link MixedMovieFinder}
     *
     * @return MixedMovieFinder
     */
    private MovieFinder makeMixedMovieFinder() {
        return new MixedMovieFinder(MIXED, Arrays.asList(make(XML), make(JSON), make(HTML)));
    }
}
