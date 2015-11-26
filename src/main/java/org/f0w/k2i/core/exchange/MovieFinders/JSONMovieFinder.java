package org.f0w.k2i.core.exchange.MovieFinders;

import com.google.common.collect.ImmutableMap;

import org.f0w.k2i.core.configuration.Configuration;
import org.f0w.k2i.core.entities.Movie;
import org.f0w.k2i.core.utils.StringHelper;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URL;
import java.util.*;

class JSONMovieFinder extends BaseMovieFinder {
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

    public JSONMovieFinder(Configuration config) {
        super(config);
    }

    @Override
    protected String buildSearchQuery(Movie movie) {
        String url = "http://www.imdb.com/xml/find?";

        Map<String, String> query = new ImmutableMap.Builder<String, String>()
                .put("q", movie.getTitle()) // Запрос
                .put("tt", "on")            // Поиск только по названиям
                .put("nr", "1")
                .put("json", "1")           // Вывод в формате JSON
                .build()
        ;

        return StringHelper.buildHttpQuery(url, query);
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
        } catch (ParseException e){
            e.printStackTrace();
        }

        return movies;
    }
}
