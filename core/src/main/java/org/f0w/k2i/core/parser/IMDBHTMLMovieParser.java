package org.f0w.k2i.core.parser;

import lombok.val;
import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

final class IMDBHTMLMovieParser extends AbstractMovieParser<Element> {
    /**
     * {@inheritDoc}
     */
    @Override
    protected Stream<Element> getStructureStream(final String data) {
        return Jsoup.parse(data)
                .select("table.findList tr td.result_text")
                .stream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Function<Element, Movie> getDataMapper() {
        return e -> new Movie(
                parseTitle(e),
                parseYear(e.text()),
                parseType(e.text()),
                null,
                parseIMDBId(e.getElementsByTag("a").first())
        );
    }

    @Override
    int parseYear(final String year) {
        String preparedYear = year;

        Matcher m = Pattern.compile("\\(([0-9]{4})\\)").matcher(year);
        while (m.find()) {
            preparedYear = m.group(1);
        }

        return super.parseYear(preparedYear);
    }

    @Override
    Movie.Type parseType(final String type) {
        if (type.contains("(TV Series)") || type.contains("(TV Mini-Series)")) {
            return Movie.Type.SERIES;
        } else if (type.contains("(Short)")) {
            return Movie.Type.SHORT;
        } else if (type.contains("(Video Game)")) {
            return Movie.Type.VIDEO_GAME;
        }

        return Movie.Type.MOVIE;
    }

    private String parseTitle(Element element) {
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

    private String parseIMDBId(Element element) {
        val imdbIdString = Optional.ofNullable(element)
                .map(e -> e.attr("href"))
                .map(e -> e.split("/"))
                .map(e -> e.length < 2 ? null : e[2])
                .orElse(null);

        return super.parseIMDBId(imdbIdString);
    }
}
