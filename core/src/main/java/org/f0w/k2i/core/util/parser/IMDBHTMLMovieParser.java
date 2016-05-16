package org.f0w.k2i.core.util.parser;

import lombok.val;
import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

final class IMDBHTMLMovieParser extends AbstractMovieParser {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> parse(final String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Document document = Jsoup.parse(data);

        return document.select("table.findList tr td.result_text")
                .stream()
                .map(e -> new Movie(
                        parseTitle(e),
                        parseYear(e.text()),
                        parseType(e.text()),
                        null,
                        parseIMDBId(e.getElementsByTag("a").first())
                ))
                .collect(Collectors.toList());
    }

    protected String parseTitle(Element element) {
        val elementText = element.text();

        if (elementText.contains("(TV Episode)")) {
            String title = elementText;

            Matcher m = Pattern.compile(
                    "\\(TV Episode\\)\\s*-(.+?)(?=\\(.*\\)\\s*(\\(TV Series\\)|\\(TV Mini-Series\\)))"
            )
                    .matcher(elementText);
            while (m.find()) {
                title = m.group(1);
            }

            return super.parseTitle(title);
        }

        val titleString = Optional.ofNullable(element.getElementsByTag("a").first())
                .map(Element::text)
                .orElse(null);

        return super.parseTitle(titleString);
    }

    @Override
    protected int parseYear(final String year) {
        String preparedYear = year;

        Matcher m = Pattern.compile("\\(([0-9]{4})\\)").matcher(year);
        while (m.find()) {
            preparedYear = m.group(1);
        }

        return super.parseYear(preparedYear);
    }

    @Override
    protected Movie.Type parseType(final String type) {
        if (type.contains("(TV Series)") || type.contains("(TV Mini-Series)")) {
            return Movie.Type.SERIES;
        } else if (type.contains("(Short)")) {
            return Movie.Type.SHORT;
        } else if (type.contains("(Video Game)")) {
            return Movie.Type.VIDEO_GAME;
        }

        return Movie.Type.MOVIE;
    }

    protected String parseIMDBId(Element element) {
        val imdbIdString = Optional.ofNullable(element)
                .map(e -> e.attr("href"))
                .map(e -> e.split("/"))
                .map(e -> e.length < 2 ? null : e[2])
                .orElse(null);

        return super.parseIMDBId(imdbIdString);
    }
}
