package org.f0w.k2i.core.exchange.finder;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.utils.exception.KinopoiskToIMDBException;
import static org.f0w.k2i.core.utils.MovieFieldsUtils.*;

class JSONMovieFinder extends AbstractMovieFinder {
    private static final ContainerFactory CONTAINER_FACTORY = new ContainerFactory() {
        @Override
        public List creatArrayContainer() {
            return new ArrayList();
        }

        @Override
        public Map createObjectContainer() {
            return new LinkedHashMap();
        }
    };

    @Inject
    public JSONMovieFinder(Config config) {
        super(config);
    }

    @Override
    protected String buildSearchQuery(Movie movie) {
        final String movieSearchLink = "http://www.imdb.com/xml/find?";

        final Map<String, String> query = new ImmutableMap.Builder<String, String>()
                .put("q", movie.getTitle()) // Запрос
                .put("tt", "on")            // Поиск только по названиям
                .put("nr", "1")
                .put("json", "1")           // Вывод в формате JSON
                .build();

        return buildURL(movieSearchLink, query);
    }

    @Override
    protected List<Movie> parseSearchResult(String result) {
        List<Movie> movies = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try {
            Map document = (Map) parser.parse(result, CONTAINER_FACTORY);

            for (Object categories : document.values()) {
                for (Object movieInfo : (List) categories) {
                    Map movieInfoObj = (Map) movieInfo;

                    movies.add(new Movie(
                            parseTitle(movieInfoObj.get("title").toString()),
                            parseYear(movieInfoObj.get("description").toString()),
                            parseIMDBId(movieInfoObj.get("id").toString())
                    ));
                }
            }
        } catch (ParseException e) {
            throw new KinopoiskToIMDBException(e);
        }

        return movies;
    }
}
