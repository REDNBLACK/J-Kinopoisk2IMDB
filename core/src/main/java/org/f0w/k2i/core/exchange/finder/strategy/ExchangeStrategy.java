package org.f0w.k2i.core.exchange.finder.strategy;

import org.f0w.k2i.core.model.entity.Movie;

import java.net.URL;
import java.util.List;
import java.util.function.Function;

import static org.f0w.k2i.core.util.MovieUtils.parseIMDBId;
import static org.f0w.k2i.core.util.MovieUtils.parseTitle;
import static org.f0w.k2i.core.util.MovieUtils.parseYear;

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
    URL buildURL(final Movie movie);

    /**
     * Parses data, implementation specific to each Format
     *
     * @param data Data
     * @return List of movies
     */
    List<Movie> parse(final String data);

    /**
     * Generic interface for parsing single movie from collection.
     * Used in {@link this#parse(String)}
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
