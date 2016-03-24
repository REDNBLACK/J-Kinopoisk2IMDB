package org.f0w.k2i.core.exchange.finder;

import com.google.common.collect.ImmutableMap;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

import static org.f0w.k2i.core.utils.MovieFieldsUtils.*;

class HTMLMovieFinder extends AbstractMovieFinder {
    @Inject
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
                        parseTitle(e.getElementsByTag("a").first().text()),
                        parseYear(prepareYear(e.text())),
                        parseIMDBId(e.getElementsByTag("a").first().attr("href").split("/")[2])
                ))
                .collect(Collectors.toList());
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
