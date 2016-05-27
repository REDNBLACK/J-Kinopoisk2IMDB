package org.f0w.k2i.core.parser;

import lombok.val;
import org.f0w.k2i.core.model.entity.Movie;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class AbstractMovieParser<T> implements MovieParser {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> parse(final String data) {
        if (!isDataValid(data)) {
            return Collections.emptyList();
        }

        return getStructureStream(data)
                .map(getDataMapper())
                .collect(Collectors.toList());
    }

    /**
     * Returns stream of parsed structure.
     *
     * @param data Data to use as stream
     * @return Stream
     */
    protected abstract Stream<T> getStructureStream(final String data);

    /**
     * Returns the data mapper for structure.
     *
     * @return Data mapper
     */
    protected abstract Function<T, Movie> getDataMapper();

    /**
     * Checks that data is valid.
     *
     * @param data Data to check
     * @return Is data valid or not
     */
    protected boolean isDataValid(final String data) {
        return data != null;
    }

    /**
     * Returns String representation of element using mapper or null
     * @param element Element
     * @param mapper Mapper
     * @param <E> Type of element
     * @return String representation of element or null
     */
    protected <E> String stringOrNull(E element, Function<E, String> mapper) {
        return Optional.ofNullable(element)
                .map(mapper)
                .orElse(null);
    }

    /**
     * Parses title, and returns "null" string if not valid
     *
     * @param title String to parseResponse
     * @return Parsed title
     */
    String parseTitle(final String title) {
        val resultTitle = String.valueOf(title).trim();

        if ("".equals(resultTitle)) {
            return "null";
        }

        return resultTitle;
    }

    /**
     * Parses year, returns 0 if it not valid
     *
     * @param year String containing year to parse
     * @return Parsed year or 0
     */
    int parseYear(final String year) {
        val yearLength = 4;

        try {
            val resultYear = String.valueOf(year).trim().substring(0, yearLength);

            return Integer.parseInt(resultYear);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return 0;
        }
    }

    /**
     * Parses type, returns {@link Movie.Type#MOVIE} if undefined
     *
     * @param type String containing type to parse
     * @return Parsed Type or {@link Movie.Type#MOVIE}
     */
    Movie.Type parseType(final String type) {
        val safeType = String.valueOf(type);

        if (safeType.contains("TV series") || safeType.contains("TV mini-series")) {
            return Movie.Type.SERIES;
        } else if (safeType.contains("documentary")) {
            return Movie.Type.DOCUMENTARY;
        } else if (safeType.contains("short")) {
            return Movie.Type.SHORT;
        } else if (safeType.contains("video game")) {
            return Movie.Type.VIDEO_GAME;
        }

        return Movie.Type.MOVIE;
    }

    /**
     * Parses IMDB ID or returns null if it not valid
     *
     * @param imdbId String to parseResponse
     * @return Parsed IMDB ID or null
     */
    String parseIMDBId(final String imdbId) {
        val resultImdbId = String.valueOf(imdbId).trim();

        if (!resultImdbId.startsWith("tt") || resultImdbId.length() < 3) {
            return null;
        }

        return resultImdbId;
    }

    /**
     * Parses rating or returns null if it not valid
     *
     * @param rating String to parseResponse
     * @return Parsed rating or null
     */
    Integer parseRating(final String rating) {
        val resultRating = String.valueOf(rating).trim();
        val zeroValues = Arrays.asList("", "null", "zero", "0");

        if (zeroValues.contains(resultRating)) {
            return null;
        }

        try {
            val result = Integer.parseInt(resultRating);

            return result <= 10 ? result : null;
        } catch (NumberFormatException ignore) {
            return null;
        }
    }
}
