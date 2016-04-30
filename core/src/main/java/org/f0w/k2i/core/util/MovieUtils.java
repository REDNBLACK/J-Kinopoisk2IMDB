package org.f0w.k2i.core.util;

import org.apache.commons.lang3.StringUtils;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.util.exception.KinopoiskToIMDBException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.f0w.k2i.core.util.exception.ExceptionUtils.uncheck;

/**
 * NullPointer safe class for checking and parsing movie fields.
 */
public final class MovieUtils {
    private MovieUtils() {
    }

    /**
     * Parses title, and returns "null" string if not valid
     *
     * @param title String to parseSearchResult
     * @return Parsed title
     */
    public static String parseTitle(String title) {
        String resultTitle = StringUtils.replaceEachRepeatedly(
                String.valueOf(title).trim(),
                new String[]{"«", "»"},
                new String[]{"", ""}
        );

        if ("".equals(resultTitle)) {
            return "null";
        }

        return resultTitle;
    }

    /**
     * Parses title, if null or empty returns "null" defaultTitle
     *
     * @param title    String to parseSearchResult
     * @param fallback Fallback string
     * @return Parsed title or defaultTitle
     */
    public static String parseTitle(String title, String fallback) {
        String resultTitle = parseTitle(title);

        if ("null".equals(resultTitle)) {
            return parseTitle(fallback);
        }

        return resultTitle;
    }

    /**
     * Parses year, returns 0 if it not valid
     *
     * @param yearString String to parseSearchResult
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
     * Parses IMDB ID or returns null if it not valid
     *
     * @param imdbId String to parseSearchResult
     * @return Parsed IMDB ID or null
     */
    public static String parseIMDBId(String imdbId) {
        String resultImdbId = String.valueOf(imdbId).trim();

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
    public static Integer parseRating(String rating) {
        String resultRating = String.valueOf(rating).trim();
        List<String> zeroValues = Arrays.asList("", "null", "zero", "0");

        if (zeroValues.contains(resultRating)) {
            return null;
        }

        try {
            Integer result = Integer.parseInt(resultRating);

            return result <= 10 ? result : null;
        } catch (NumberFormatException ignore) {
            return null;
        }
    }

    /**
     * Checks that title equals "null" string
     *
     * @param title String to check
     * @return Is title empty
     */
    public static boolean isEmptyTitle(String title) {
        return "null".equals(title);
    }

    /**
     * Checks that year equals 0
     *
     * @param year Integer to check
     * @return Is year empty
     */
    public static boolean isEmptyYear(int year) {
        return year == 0;
    }

    /**
     * Checks that IMDB ID is null
     *
     * @param imdbId String to check
     * @return Is IMDB ID null
     */
    public static boolean isEmptyIMDBId(String imdbId) {
        return imdbId == null;
    }

    /**
     * Checks that rating is null
     *
     * @param rating String to check
     * @return Is rating null
     */
    public static boolean isEmptyRating(Integer rating) {
        return rating == null;
    }

    /**
     * Parses movies from file
     *
     * @param filePath File to parseSearchResult
     * @return List of parsed movies
     * @throws KinopoiskToIMDBException If an I/O error occurs
     */
    public static List<Movie> parseMovies(Path filePath) {
        String data = uncheck(() -> new String(Files.readAllBytes(filePath), Charset.forName("windows-1251")));
        Elements content = Jsoup.parse(data).select("table tr");

        if (content.isEmpty()) {
            throw new KinopoiskToIMDBException(String.format("File '%s' is not valid or empty!", filePath));
        }

        List<String> header = content.remove(0)
                .getElementsByTag("td")
                .stream()
                .map(Element::text)
                .collect(Collectors.toList());

        List<Map<String, String>> elements = content.stream()
                .map(e -> e.getElementsByTag("td")
                        .stream()
                        .map(Element::text)
                        .collect(Collectors.toList())
                )
                .map(e -> CollectionUtils.combineLists(header, e))
                .collect(Collectors.toList());

        return elements.stream()
                .map(m -> new Movie(
                        parseTitle(m.get("оригинальное название"), m.get("русскоязычное название")),
                        parseYear(m.get("год")),
                        parseRating(m.get("моя оценка"))
                ))
                .collect(Collectors.toList());
    }
}
