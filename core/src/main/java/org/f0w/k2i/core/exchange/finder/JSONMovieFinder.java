package org.f0w.k2i.core.exchange.finder;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.util.exception.KinopoiskToIMDBException;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

import static org.f0w.k2i.core.util.HttpUtils.buildURL;
import static org.f0w.k2i.core.util.MovieUtils.*;

final class JSONMovieFinder extends AbstractMovieFinder {
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
        super(config);
    }

    /** {@inheritDoc} */
    @Override
    protected String buildSearchQuery(Movie movie) {
        final String movieSearchLink = "http://www.imdb.com/xml/find?";

        final Map<String, String> query = new ImmutableMap.Builder<String, String>()
                .put("q", movie.getTitle())
                .put("tt", "on")
                .put("nr", "1")
                .put("json", "1")
                .build();

        return buildURL(movieSearchLink, query);
    }

    /** {@inheritDoc} */
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
                            parseTitle(getStringValueOrNull(movieInfoObj.get("title"))),
                            parseYear(getStringValueOrNull(movieInfoObj.get("description"))),
                            parseIMDBId(getStringValueOrNull(movieInfoObj.get("id")))
                    ));
                }
            }
        } catch (ParseException e) {
            throw new KinopoiskToIMDBException(e);
        }

        return movies;
    }

    private static String getStringValueOrNull(Object object) {
        return Optional.ofNullable(object)
                .map(String::valueOf)
                .orElse(null);
    }
}
