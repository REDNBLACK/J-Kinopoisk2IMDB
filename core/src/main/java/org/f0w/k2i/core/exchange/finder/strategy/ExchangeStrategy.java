package org.f0w.k2i.core.exchange.finder.strategy;

import lombok.NonNull;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.util.MovieUtils;

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
     */
    URL buildSearchURL(@NonNull final Movie movie);

    /**
     * Parses data, implementation specific to each Format
     *
     * @param data Data
     * @return List of movies if data not an empty string, or {@link Collections#emptyList()} otherwise.
     */
    List<Movie> parseSearchResult(@NonNull final String data);

    /**
     * Generic interface for parsing single movie from collection.
     * Used in {@link this#parseSearchResult(String)}
     *
     * @param <R> Root element
     * @param <TITLE> Title element
     * @param <YEAR> Year element
     * @param <YEAR> Type element
     * @param <ID> IMDB ID element
     */
    interface MovieParser<R, TITLE, YEAR, TYPE, ID> {
        default Movie parse(
                R rootElement,
                Function<R, TITLE> title,
                Function<R, YEAR> year,
                Function<R, TYPE> type,
                Function<R, ID> imdbID
        ) {
            return new Movie(
                    parseTitle(prepareTitle(title.apply(rootElement))),
                    parseYear(prepareYear(year.apply(rootElement))),
                    parseType(type.apply(rootElement)),
                    null,
                    parseIMDBId(prepareImdbId(imdbID.apply(rootElement)))
            );
        }

        String prepareTitle(TITLE element);

        String prepareYear(YEAR element);

        Movie.Type parseType(TYPE element);

        String prepareImdbId(ID element);
    }
}
