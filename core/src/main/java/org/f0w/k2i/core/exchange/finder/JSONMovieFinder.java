package org.f0w.k2i.core.exchange.finder;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

final class JSONMovieFinder extends AbstractMovieFinder {
    private static final String SEARCH_LINK = "http://www.imdb.com/xml/find?";
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Movie.class, new MovieDeserializer())
            .create();

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
        JsonParser parser = new JsonParser();

        return parser.parse(result)
                .getAsJsonObject()
                .entrySet()
                .stream()
                .map(e -> e.getValue().getAsJsonArray())
                .flatMap(a -> StreamSupport.stream(a.spliterator(), false))
                .map(e -> GSON.fromJson(e, Movie.class))
                .collect(Collectors.toList());
    }

    private static final class MovieDeserializer implements JsonDeserializer<Movie> {
        @Override
        public Movie deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) {
            return new JSONMovieParser(json).parse(
                    t -> t.get("title"),
                    t -> t.get("description"),
                    t -> t.get("id")
            );
        }
    }

    private static final class JSONMovieParser extends MovieParser<JsonObject, JsonElement, JsonElement, JsonElement> {
        public JSONMovieParser(JsonElement root) {
            super(root.getAsJsonObject());
        }

        @Override
        public String prepareTitle(JsonElement element) {
            return getStringOrNull(element);
        }

        @Override
        public String prepareYear(JsonElement element) {
            return getStringOrNull(element);
        }

        @Override
        public String prepareImdbId(JsonElement element) {
            return getStringOrNull(element);
        }

        private String getStringOrNull(JsonElement element) {
            return Optional.ofNullable(element)
                    .map(JsonElement::getAsString)
                    .orElse(null);
        }
    }
}
