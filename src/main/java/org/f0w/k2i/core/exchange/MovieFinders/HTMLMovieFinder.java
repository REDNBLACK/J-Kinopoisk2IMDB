package org.f0w.k2i.core.exchange.MovieFinders;

import com.google.common.collect.ImmutableMap;

import org.f0w.k2i.core.configuration.Configuration;
import org.f0w.k2i.core.entities.Movie;

import org.f0w.k2i.core.utils.StringHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;
import java.util.regex.*;
import java.nio.charset.StandardCharsets;

class HTMLMovieFinder extends BaseMovieFinder {
    public HTMLMovieFinder(Configuration config) {
        super(config);
    }

    @Override
    protected String buildSearchQuery(Movie movie) {
        String url = "http://www.imdb.com/find?";

        Map<String, String> query = new ImmutableMap.Builder<String, String>()
                .put("q", movie.getTitle()) // Запрос
                .put("s", "tt")             // Поиск только по названиям
//                .put("exact", "true")     // Поиск только по полным совпадениям
                .put("ref", "fn_tt_ex")     // Реферер для надежности
                .build()
        ;

        return StringHelper.buildHttpQuery(url, query);
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
