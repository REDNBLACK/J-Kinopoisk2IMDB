package org.f0w.k2i.core.exchange.MovieFinders;

import com.google.common.collect.ImmutableMap;

import org.f0w.k2i.core.configuration.Configuration;
import org.f0w.k2i.core.entities.Movie;

import org.f0w.k2i.core.utils.RequestHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.*;

class XMLMovieFinder extends BaseMovieFinder {
    public XMLMovieFinder(Configuration config) {
        super(config);
    }

    @Override
    protected URL buildSearchQuery(Movie movie) {
        String url = "http://www.imdb.com/xml/find?";

        Map<String, String> query = new ImmutableMap.Builder<String, String>()
                .put("q", movie.getTitle()) // Запрос
                .put("tt", "on")            // Поиск только по названиям
                .put("nr", "1")
                .build()
        ;

        return RequestHelper.makeURL(url, query);
    }

    @Override
    protected List<Movie> parseSearchResult(String result) {
        List<Movie> movies = new ArrayList<>();
        Document document = Jsoup.parse(result, "UTF-8");

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
