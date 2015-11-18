package org.f0w.k2i.core.exchange.MovieFinders;

import com.google.common.collect.ImmutableMap;
import org.f0w.k2i.core.Components.Configuration;
import org.f0w.k2i.core.net.HttpRequest;
import org.f0w.k2i.core.entities.Movie;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JSONMovieFinder extends BaseMovieFinder {
    public JSONMovieFinder(HttpRequest request, Configuration config) {
        super(request, config);
    }

    @Override
    protected URL buildSearchQuery(Movie movie) {
        String url = "http://www.imdb.com/xml/find?";

        Map<String, String> query = new ImmutableMap.Builder<String, String>()
                .put("q", movie.getTitle()) // Запрос
                .put("tt", "on")            // Поиск только по названиям
                .put("nr", "1")
                .put("json", "1")           // Вывод в формате JSON
                .build()
        ;

        return request.makeURL(url, query);
    }

    @Override
    protected List<Movie> parseSearchResult(String result) {
        List<Movie> movies = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try {
            Map document = (Map) parser.parse(result, new ContainerFactory() {
                @Override
                public List creatArrayContainer() {
                    return new ArrayList();
                }

                @Override
                public Map createObjectContainer() {
                    return new LinkedHashMap();
                }
            });

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
