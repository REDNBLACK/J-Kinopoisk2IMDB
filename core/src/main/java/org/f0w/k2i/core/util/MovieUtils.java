package org.f0w.k2i.core.util;

import com.google.common.base.Strings;

public final class MovieUtils {
    private MovieUtils() {
        throw new UnsupportedOperationException();
    }

    public static String parseTitle(String title) {
        String resultTitle = String.valueOf(title).trim();

        if ("".equals(resultTitle)) {
            return "null";
        }

        return resultTitle;
    }

    public static String parseTitle(String title, String defaultTitle) {
        String resultTitle = parseTitle(title);

        if ("null".equals(resultTitle)) {
            return parseTitle(defaultTitle);
        }

        return resultTitle;
    }

    public static boolean isEmptyTitle(String title) {
        return "null".equals(title);
    }

    public static int parseYear(String yearString) {
        final int yearLength = 4;

        try {
            String resultYear = String.valueOf(yearString).trim().substring(0, yearLength);

            return Integer.parseInt(resultYear);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return 0;
        }
    }

    public static boolean isEmptyYear(int year) {
        return year == 0;
    }

    public static String parseIMDBId(String imdbId) {
        String resultImdbId = String.valueOf(imdbId).trim();

        if (!resultImdbId.startsWith("tt")) {
            return null;
        }

        return resultImdbId;
    }

    public static boolean isEmptyIMDBId(String imdbId) {
        return imdbId == null;
    }

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

    public static boolean isEmptyRating(Integer rating) {
        return rating == null;
    }
}
