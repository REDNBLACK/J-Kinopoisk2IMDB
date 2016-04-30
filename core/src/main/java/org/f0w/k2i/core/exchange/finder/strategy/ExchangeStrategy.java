package org.f0w.k2i.core.exchange.finder.strategy;

import org.f0w.k2i.core.model.entity.Movie;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.f0w.k2i.core.util.MovieUtils.*;

/**
 * Exchange strategy for MovieFinder
 */
public interface ExchangeStrategy {
    /**
     * Build movie search URL based on movie fields, implementation specific to each Format.
     *
     * @param movie Movie which fields to use
     * @return Movie search URL
     * @throws NullPointerException If movie is null
     */
    URL buildSearchURL(final Movie movie);

    /**
     * Parses data, implementation specific to each Format
     *
     * @param data Data
     * @return List of movies if data not an empty string, or {@link Collections#emptyList()} otherwise.
     * @throws NullPointerException If data is null
     */
    List<Movie> parseSearchResult(final String data);

    /**
     * Generic interface for parsing single movie from collection.
     * Used in {@link this#parseSearchResult(String)}
     * @param <R> Root element
     * @param <T> Title element
     * @param <Y> Year element
     * @param <I> IMDB ID element
     */
    interface MovieParser<R, T, Y, I> {
        default Movie parse(R rootElement, Function<R, T> title, Function<R, Y> year, Function<R, I> imdbID) {
            return new Movie(
                    parseTitle(prepareTitle(title.apply(rootElement))),
                    parseYear(prepareYear(year.apply(rootElement))),
                    parseIMDBId(prepareImdbId(imdbID.apply(rootElement)))
            );
        }

        String prepareTitle(T element);

        String prepareYear(Y element);

        String prepareImdbId(I element);
    }
}
