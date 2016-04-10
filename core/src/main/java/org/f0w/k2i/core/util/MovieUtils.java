package org.f0w.k2i.core.util;

import com.google.common.base.Strings;

/**
 * NullPointer safe class for checking and parsing movie fields.
 */
public final class MovieUtils {
    private MovieUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Parses title, and returns "null" string if not valid
     * @param title String to parse
     * @return Parsed title
     */
    public static String parseTitle(String title) {
        String resultTitle = String.valueOf(title).trim();

        if ("".equals(resultTitle)) {
            return "null";
        }

        return resultTitle;
    }

    /**
     * Parses title, if null or empty returns "null" defaultTitle
     * @param title String to parse
     * @param defaultTitle Fallback string
     * @return Parsed title or defaultTitle
     */
    public static String parseTitle(String title, String defaultTitle) {
        String resultTitle = parseTitle(title);

        if ("null".equals(resultTitle)) {
            return parseTitle(defaultTitle);
        }

        return resultTitle;
    }

    /**
     * Checks that title equals "null" string
     * @param title String to check
     * @return Is title empty
     */
    public static boolean isEmptyTitle(String title) {
        return "null".equals(title);
    }

    /**
     * Parses year, returns 0 if it not valid
     * @param yearString String to parse
     * @return Parsed year or 0
     */
    public static int parseYear(String yearString) {
        final int yearLength = 4;

        try {
            String resultYear = String.valueOf(yearString).trim().substring(0, yearLength);

            return Integer.parseInt(resultYear);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return 0;
        }
    }

    /**
     * Checks that year equals 0
     * @param year Integer to check
     * @return Is year empty
     */
    public static boolean isEmptyYear(int year) {
        return year == 0;
    }

    /**
     * Parses IMDB ID or returns null if it not valid
     * @param imdbId String to parse
     * @return Parsed IMDB ID or null
     */
    public static String parseIMDBId(String imdbId) {
        String resultImdbId = String.valueOf(imdbId).trim();

        if (!resultImdbId.startsWith("tt")) {
            return null;
        }

        return resultImdbId;
    }

    /**
     * Checks that IMDB ID is null
     * @param imdbId String to check
     * @return Is IMDB ID null
     */
    public static boolean isEmptyIMDBId(String imdbId) {
        return imdbId == null;
    }

    /**
     * Parses rating or returns null if it not valid
     * @param rating String to parse
     * @return Parsed rating or null
     */
    public static Integer parseRating(String rating) {
        String resultRating = String.valueOf(rating).trim();

        if (Strings.isNullOrEmpty(resultRating)
            || "null".equals(resultRating)
            || "zero".equals(resultRating)
            || "0".equals(resultRating)
        ) {
            return null;
        }

        try {
            return Integer.parseInt(resultRating);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Checks that rating is null
     * @param rating String to check
     * @return Is rating null
     */
    public static boolean isEmptyRating(Integer rating) {
        return rating == null;
    }
}
