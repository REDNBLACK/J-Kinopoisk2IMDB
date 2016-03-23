package org.f0w.k2i.core.exchange.finder;

import com.google.common.collect.ImmutableMap;

import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.utils.exception.KinopoiskToIMDBException;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

class JSONMovieFinder extends AbstractMovieFinder {
    private static final ContainerFactory containerJSONFactory = new ContainerFactory() {
        @Override
        public List creatArrayContainer() {
            return new ArrayList();
        }

        @Override
        public Map createObjectContainer() {
            return new LinkedHashMap();
        }
    };

    @Override
    protected String buildSearchQuery(Movie movie) {
        final String url = "http://www.imdb.com/xml/find?";

        final Map<String, String> query = new ImmutableMap.Builder<String, String>()
                .put("q", movie.getTitle()) // Запрос
                .put("tt", "on")            // Поиск только по названиям
                .put("nr", "1")
                .put("json", "1")           // Вывод в формате JSON
                .build();

        return buildHttpQuery(url, query);
    }

    @Override
    protected List<Movie> parseSearchResult(String result) {
        List<Movie> movies = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try {
            Map document = (Map) parser.parse(result, containerJSONFactory);

            for (Object categories : document.values()) {
                for (Object movieInfo : (List) categories) {
                    Map movieInfoObj = (Map) movieInfo;

                    Movie movie = new Movie();
                    movie.setTitle(movieInfoObj.get("title").toString());
                    movie.setYear(Integer.parseInt(movieInfoObj.get("description").toString().substring(0, 4)));
                    movie.setImdbId(movieInfoObj.get("id").toString());

                    movies.add(movie);
                }
            }
        } catch (ParseException e) {
            throw new KinopoiskToIMDBException(e);
        }

        return movies;
    }
}
