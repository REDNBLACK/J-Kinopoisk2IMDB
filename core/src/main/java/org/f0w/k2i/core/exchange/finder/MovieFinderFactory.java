package org.f0w.k2i.core.exchange.finder;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import lombok.val;
import org.f0w.k2i.core.DocumentSourceType;
import org.f0w.k2i.core.exchange.finder.strategy.IMDBHTMLExchangeStrategy;
import org.f0w.k2i.core.exchange.finder.strategy.IMDBJSONExchangeStrategy;
import org.f0w.k2i.core.exchange.finder.strategy.IMDBXMLExchangeStrategy;
import org.f0w.k2i.core.exchange.finder.strategy.OMDBExchangeStrategy;

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
            case IMDB_XML:
                return new BasicMovieFinder(IMDB_XML, new IMDBXMLExchangeStrategy(), config);
            case IMDB_JSON:
                return new BasicMovieFinder(IMDB_JSON, new IMDBJSONExchangeStrategy(), config);
            case IMDB_HTML:
                return new BasicMovieFinder(IMDB_HTML, new IMDBHTMLExchangeStrategy(), config);
            case OMDB:
                return new BasicMovieFinder(OMDB, new OMDBExchangeStrategy(), config);
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
