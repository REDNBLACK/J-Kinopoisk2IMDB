package org.f0w.k2i.core.exchange.finder;

import com.google.common.collect.ImmutableMap;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.*;
import java.util.stream.Collectors;

import static org.f0w.k2i.core.utils.MovieFieldsUtils.*;

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
        Document document = Jsoup.parse(result);

        return document.getElementsByTag("ImdbEntity")
                .stream()
                .map(e -> new Movie(
                        parseTitle(e.ownText()),
                        parseYear(e.getElementsByTag("Description").first().text()),
                        parseIMDBId(e.attr("id"))
                ))
                .collect(Collectors.toList());
    }
}
