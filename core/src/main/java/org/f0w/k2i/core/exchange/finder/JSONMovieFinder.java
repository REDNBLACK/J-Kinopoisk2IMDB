package org.f0w.k2i.core.exchange.finder;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.util.exception.KinopoiskToIMDBException;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

final class JSONMovieFinder extends AbstractMovieFinder {
    private static final String SEARCH_LINK = "http://www.imdb.com/xml/find?";

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

    public JSONMovieFinder(Config config) {
        super(config, SEARCH_LINK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> buildSearchQuery(Movie movie) {
        return new ImmutableMap.Builder<String, String>()
                .put("q", movie.getTitle())
                .put("tt", "on")
                .put("nr", "1")
                .put("json", "1")
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> parseSearchResult(String result) {
        List<Movie> movies = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try {
            Map document = (Map) parser.parse(result, CONTAINER_FACTORY);

            for (Object categories : document.values()) {
                for (Object movieInfo : (List) categories) {
                    Map movieInfoObj = (Map) movieInfo;

                    movies.add(new JSONMovieParser(movieInfoObj).parse(
                            t -> t.get("title"),
                            t -> t.get("description"),
                            t -> t.get("id")
                    ));
                }
            }
        } catch (ParseException e) {
            throw new KinopoiskToIMDBException(e);
        }

        return movies;
    }

    private static final class JSONMovieParser extends MovieParser<Map<?, ?>, Object, Object, Object> {
        public JSONMovieParser(Map<?, ?> root) {
            super(root);
        }

        @Override
        public String prepareTitle(Object element) {
            return getStringValueOrNull(element);
        }

        @Override
        public String prepareYear(Object element) {
            return getStringValueOrNull(element);
        }

        @Override
        public String prepareImdbId(Object element) {
            return getStringValueOrNull(element);
        }

        private String getStringValueOrNull(Object object) {
            return Optional.ofNullable(object)
                    .map(String::valueOf)
                    .orElse(null);
        }
    }
}
