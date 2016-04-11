package org.f0w.k2i.core.exchange.finder;

import com.google.common.collect.ImmutableMap;

import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

import static org.f0w.k2i.core.util.MovieUtils.*;

final class HTMLMovieFinder extends AbstractMovieFinder {
    public HTMLMovieFinder(Config config) {
        super(config);
    }

    @Override
    protected String buildSearchQuery(Movie movie) {
        final String movieSearchLink = "http://www.imdb.com/find?";

        final Map<String, String> query = new ImmutableMap.Builder<String, String>()
                .put("q", movie.getTitle()) // Запрос
                .put("s", "tt")             // Поиск только по названиям
              //.put("exact", "true")       // Поиск только по полным совпадениям
                .put("ref", "fn_tt_ex")     // Реферер для надежности
                .build();

        return buildURL(movieSearchLink, query);
    }

    @Override
    protected List<Movie> parseSearchResult(String result) {
        Document document = Jsoup.parse(result);

        return document.select("table.findList tr td.result_text")
                .stream()
                .map(e -> new Movie(
                        parseTitle(prepareTitle(e)),
                        parseYear(prepareYear(e.text())),
                        parseIMDBId(prepareImdbId(e.getElementsByTag("a").first()))
                ))
                .collect(Collectors.toList());
    }

    private static String prepareImdbId(Element element) {
        return Optional.ofNullable(element)
                .map(e -> e.attr("href"))
                .map(e -> e.split("/"))
                .map(e -> e.length < 2 ? null : e[2])
                .orElse(null);
    }

    private static String prepareTitle(Element element) {
        String elementText = element.text();

        if (elementText.contains("(TV Episode)")) {
            String title = elementText;

            Matcher m = Pattern.compile("\\(TV Episode\\)\\s*-(.+?)(?=\\(.*\\)\\s*\\(TV Series\\))")
                    .matcher(elementText);
            while (m.find()) {
                 title = m.group(1);
            }

            return title;
        }

        return Optional.ofNullable(element.getElementsByTag("a").first())
                .map(Element::text)
                .orElse(null);
    }

    private static String prepareYear(String year) {
        String preparedYear = year;

        Matcher m = Pattern.compile("\\(([0-9]{4})\\)").matcher(year);
        while (m.find()) {
            preparedYear = m.group(1);
        }

        return preparedYear;
    }
}
