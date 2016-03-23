package org.f0w.k2i.core.exchange.finder;

import com.google.common.collect.ImmutableMap;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;
import java.util.regex.*;
import java.nio.charset.StandardCharsets;

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
        List<Movie> movies = new ArrayList<>();
        Document document = Jsoup.parse(result, StandardCharsets.UTF_8.name());

        for (Element element : document.select("table.findList tr td.result_text")) {
            Element link = element.getElementsByTag("a").first();

            Movie movie = new Movie();
            movie.setTitle(link.text());
            movie.setImdbId(link.attr("href").split("/")[2]);
            Matcher m = Pattern.compile("\\(([0-9]{4})\\)").matcher(element.text());
            while (m.find()) {
                movie.setYear(Integer.parseInt(m.group(1)));
            }

            movies.add(movie);
        }

        return movies;
    }
}
