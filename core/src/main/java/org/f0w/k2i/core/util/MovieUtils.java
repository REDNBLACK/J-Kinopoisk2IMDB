package org.f0w.k2i.core.util;

import lombok.val;
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

import static org.apache.commons.lang3.StringUtils.replaceEachRepeatedly;
import static org.apache.commons.lang3.StringUtils.splitByWholeSeparator;
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
    public static String parseTitle(final String title) {
        val resultTitle = String.valueOf(title).trim();

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
    public static String parseTitle(final String title, final String fallback) {
        val resultTitle = parseTitle(title);

        if ("null".equals(resultTitle)) {
            return replaceEachRepeatedly(
                    parseTitle(fallback),
                    new String[]{"«", "»"},
                    new String[]{"", ""}
            );
        }

        return resultTitle;
    }

    /**
     * Parses year, returns 0 if it not valid
     *
     * @param yearString String to parseSearchResult
     * @return Parsed year or 0
     */
    public static int parseYear(final String yearString) {
        val yearLength = 4;

        try {
            val resultYear = String.valueOf(yearString).trim().substring(0, yearLength);

            return Integer.parseInt(resultYear);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return 0;
        }
    }

    private static Movie.Type parseType(final Map<String, String> row) {
        val genres = Arrays.asList(splitByWholeSeparator(row.get("жанры"), ", "));
        val isSeries = row.get("год").split("-|–").length == 2;

        if (isSeries) {
            return Movie.Type.SERIES;
        }

        if (genres.contains("документальный")) {
            return Movie.Type.DOCUMENTARY;
        } else if (genres.contains("короткометражка")) {
            return Movie.Type.SHORT;
        }

        return Movie.Type.MOVIE;
    }

    /**
     * Parses IMDB ID or returns null if it not valid
     *
     * @param imdbId String to parseSearchResult
     * @return Parsed IMDB ID or null
     */
    public static String parseIMDBId(final String imdbId) {
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
    public static Integer parseRating(final String rating) {
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

    /**
     * Parses movies from file
     *
     * @param filePath File to parseSearchResult
     * @return List of parsed movies
     * @throws KinopoiskToIMDBException If an I/O error occurs
     */
    public static List<Movie> parseMovies(final Path filePath) {
        val data = uncheck(() -> new String(Files.readAllBytes(filePath), Charset.forName("windows-1251")));
        Elements content = Jsoup.parse(data).select("table tr");

        if (content.isEmpty()) {
            throw new KinopoiskToIMDBException(String.format("File '%s' is not valid or empty!", filePath));
        }

        val header = content.remove(0)
                .getElementsByTag("td")
                .stream()
                .map(Element::text)
                .collect(Collectors.toList());

        val rows = content.stream()
                .map(e -> e.getElementsByTag("td")
                        .stream()
                        .map(Element::text)
                        .collect(Collectors.toList())
                )
                .map(e -> CollectionUtils.combineLists(header, e))
                .collect(Collectors.toList());

        return rows.stream()
                .map(m -> new Movie(
                        parseTitle(m.get("оригинальное название"), m.get("русскоязычное название")),
                        parseYear(m.get("год")),
                        parseType(m),
                        parseRating(m.get("моя оценка")),
                        null
                ))
                .collect(Collectors.toList());
    }
}
