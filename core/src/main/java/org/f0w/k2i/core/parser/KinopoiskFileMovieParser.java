package org.f0w.k2i.core.parser;

import lombok.val;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.util.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.replaceEachRepeatedly;

final class KinopoiskFileMovieParser extends AbstractMovieParser<Map<String, String>> {
    /**
     * {@inheritDoc}
     */
    @Override
    protected Stream<Map<String, String>> getStructureStream(final String data) {
        Elements content = Jsoup.parse(data).select("table tr");

        if (content.isEmpty()) {
            return Stream.empty();
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

        return rows.stream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Function<Map<String, String>, Movie> getDataMapper() {
        return e -> new Movie(
                parseTitle(e.get("оригинальное название"), e.get("русскоязычное название")),
                parseYear(e.get("год")),
                parseType(e),
                parseRating(e.get("моя оценка")),
                null
        );
    }

    /**
     * Parses title, if null or empty returns "null" defaultTitle
     *
     * @param title    String to parseResponse
     * @param fallback Fallback string
     * @return Parsed title or defaultTitle
     */
    private String parseTitle(final String title, final String fallback) {
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

    private Movie.Type parseType(final Map<String, String> row) {
        boolean isSeries = row.get("год").split("-|–").length == 2;
        if (isSeries) {
            return Movie.Type.SERIES;
        }

        val genres = row.get("жанры");
        if (genres.contains("документальный")) {
            return Movie.Type.DOCUMENTARY;
        } else if (genres.contains("короткометражка")) {
            return Movie.Type.SHORT;
        }

        return Movie.Type.MOVIE;
    }
}
