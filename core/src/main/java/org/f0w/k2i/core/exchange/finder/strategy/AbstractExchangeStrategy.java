package org.f0w.k2i.core.exchange.finder.strategy;

import lombok.NonNull;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.parser.MovieParser;
import org.jsoup.Connection;

import java.util.List;

abstract class AbstractExchangeStrategy implements ExchangeStrategy {
    @NonNull
    private final MovieParser parser;

    public AbstractExchangeStrategy(MovieParser parser) {
        this.parser = parser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> parseResponse(@NonNull Connection.Response response) {
        return parser.parse(response.body());
    }
}
