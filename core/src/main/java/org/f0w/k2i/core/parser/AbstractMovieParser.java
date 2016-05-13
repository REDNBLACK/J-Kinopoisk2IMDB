package org.f0w.k2i.core.parser;

import lombok.val;
import org.f0w.k2i.core.model.entity.Movie;

import java.util.Arrays;

abstract class AbstractMovieParser implements MovieParser {
    /**
     * Parses title, and returns "null" string if not valid
     *
     * @param title String to parseSearchResult
     * @return Parsed title
     */
    protected String parseTitle(final String title) {
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
    protected int parseYear(final String year) {
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
    protected Movie.Type parseType(final String type) {
        if (type.contains("TV series") || type.contains("TV mini-series")) {
            return Movie.Type.SERIES;
        } else if (type.contains("documentary")) {
            return Movie.Type.DOCUMENTARY;
        } else if (type.contains("short")) {
            return Movie.Type.SHORT;
        } else if (type.contains("video game")) {
            return Movie.Type.VIDEO_GAME;
        }

        return Movie.Type.MOVIE;
    }

    /**
     * Parses IMDB ID or returns null if it not valid
     *
     * @param imdbId String to parseSearchResult
     * @return Parsed IMDB ID or null
     */
    protected String parseIMDBId(final String imdbId) {
        val resultImdbId = String.valueOf(imdbId).trim();

        if (!resultImdbId.startsWith("tt") || resultImdbId.length() < 3) {
            return null;
        }

        return resultImdbId;
    }

    /**
     * Parses rating or returns null if it not valid
     *
     * @param rating String to parseSearchResult
     * @return Parsed rating or null
     */
    protected Integer parseRating(final String rating) {
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
