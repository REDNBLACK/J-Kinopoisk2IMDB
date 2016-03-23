package org.f0w.k2i.core.exchange.finder;

import com.google.common.collect.ImmutableMap;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.nio.charset.StandardCharsets;
import java.util.*;

class XMLMovieFinder extends AbstractMovieFinder {
    @Inject
    public XMLMovieFinder(Config config) {
        super(config);
    }

    @Override
    protected String buildSearchQuery(Movie movie) {
        String movieSearchLink = "http://www.imdb.com/xml/find?";

        Map<String, String> query = new ImmutableMap.Builder<String, String>()
                .put("q", movie.getTitle()) // Запрос
                .put("tt", "on")            // Поиск только по названиям
                .put("nr", "1")
                .build();

        return buildURL(movieSearchLink, query);
    }

    @Override
    protected List<Movie> parseSearchResult(String result) {
        List<Movie> movies = new ArrayList<>();
        Document document = Jsoup.parse(result, StandardCharsets.UTF_8.name());

        for (Element element : document.getElementsByTag("ImdbEntity")) {
            Movie movie = new Movie();
            movie.setTitle(element.ownText());
            movie.setYear(Integer.parseInt(element.getElementsByTag("Description").first().text().substring(0, 4)));
            movie.setImdbId(element.attr("id"));

            movies.add(movie);
        }

        return movies;
    }
}
