package org.f0w.k2i.core.utils;

public class MovieFieldsUtils {
    private MovieFieldsUtils() {
        throw new UnsupportedOperationException();
    }

    public static String parseTitle(String title) {
        title = String.valueOf(title).trim();

        if ("".equals(title)) {
            return "null";
        }

        return title;
    }

    public static boolean isEmptyTitle(String title) {
        return "null".equals(title);
    }

    public static int parseYear(String yearString) {
        try {
            yearString = String.valueOf(yearString).trim().substring(0, 4);

            return Integer.parseInt(yearString);
        } catch (NumberFormatException|IndexOutOfBoundsException e) {
            return 0;
        }
    }

    public static boolean isEmptyYear(int year) {
        return year == 0;
    }

    public static String parseIMDBId(String imdbId) {
        imdbId = String.valueOf(imdbId).trim();

        if (!imdbId.startsWith("tt")) {
            return null;
        }

        return imdbId;
    }

    public static boolean isEmptyIMDBId(String imdbId) {
        return imdbId == null;
    }
}
