package org.f0w.k2i.core.util.parser;

import lombok.val;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.util.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.replaceEachRepeatedly;
import static org.apache.commons.lang3.StringUtils.splitByWholeSeparator;

final class KinopoiskFileMovieParser extends AbstractMovieParser {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> parse(final String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Elements content = Jsoup.parse(data).select("table tr");

        if (content.isEmpty()) {
            return Collections.emptyList();
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

    /**
     * Parses title, if null or empty returns "null" defaultTitle
     *
     * @param title    String to parseResponse
     * @param fallback Fallback string
     * @return Parsed title or defaultTitle
     */
    protected String parseTitle(final String title, final String fallback) {
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

    protected Movie.Type parseType(final Map<String, String> row) {
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
