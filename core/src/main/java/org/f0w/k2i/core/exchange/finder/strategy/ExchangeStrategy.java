package org.f0w.k2i.core.exchange.finder.strategy;

import lombok.NonNull;
import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Connection;

import java.util.Collections;
import java.util.List;

/**
 * Exchange strategy for MovieFinder
 */
public interface ExchangeStrategy {
    /**
     * Build movie search URL based on movie fields, implementation specific to each Format.
     *
     * @param movie Movie which fields to use
     * @return Movie search URL
     */
    Connection.Request buildRequest(@NonNull final Movie movie);

    /**
     * Parses response, implementation specific to each Format
     *
     * @param response Response
     * @return List of movies if data not an empty string, or {@link Collections#emptyList()} otherwise.
     */
    List<Movie> parseResponse(@NonNull final Connection.Response response);
}
