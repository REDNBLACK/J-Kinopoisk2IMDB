package org.f0w.k2i.core.exchange.finder;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import lombok.val;
import org.f0w.k2i.core.DocumentSourceType;
import org.f0w.k2i.core.exchange.finder.strategy.HTMLExchangeStrategy;
import org.f0w.k2i.core.exchange.finder.strategy.JSONExchangeStrategy;
import org.f0w.k2i.core.exchange.finder.strategy.XMLExchangeStrategy;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.f0w.k2i.core.DocumentSourceType.*;

/**
 * {@link MovieFinder} factory
 */
public class MovieFinderFactory {
    private final Config config;

    @Inject
    public MovieFinderFactory(Config config) {
        this.config = config;
    }

    /**
     * Create instance of {@link MovieFinder},
     * using {@link DocumentSourceType} as argument.
     *
     * @param documentSourceType Type of Document source
     * @return MovieFinder instance
     */
    public MovieFinder make(DocumentSourceType documentSourceType) {
        switch (documentSourceType) {
            case XML:
                return new BasicMovieFinder(XML, new XMLExchangeStrategy(), config);
            case JSON:
                return new BasicMovieFinder(JSON, new JSONExchangeStrategy(), config);
            case HTML:
                return new BasicMovieFinder(HTML, new HTMLExchangeStrategy(), config);
            default:
                throw new IllegalArgumentException("Invalid movie finder type!");
        }
    }

    /**
     * Create instance of mixed {@link MovieFinder},
     * using multiple {@link DocumentSourceType} as argument.
     *
     * @param documentSourceTypes Types of Document source
     * @return MovieFinder instance
     */
    public MovieFinder make(DocumentSourceType... documentSourceTypes) {
        val distinctTypes = Arrays.asList(documentSourceTypes)
                .stream()
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        if (distinctTypes.size() == 1) {
            return make(distinctTypes.get(0));
        }

        return new MixedMovieFinder(distinctTypes.stream()
                .map(this::make)
                .toArray(MovieFinder[]::new)
        );
    }
}
