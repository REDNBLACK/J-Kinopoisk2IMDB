package org.f0w.k2i.core.exchange.finder.strategy;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.util.HttpUtils;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class JSONExchangeStrategy implements ExchangeStrategy {
    /**
     * {@inheritDoc}
     */
    @Override
    public URL buildURL(final Movie movie) {
        String searchLink = "http://www.imdb.com/xml/find?";
        Map<String, String> queryParams = new ImmutableMap.Builder<String, String>()
                .put("q", movie.getTitle())
                .put("tt", "on")
                .put("nr", "1")
                .put("json", "1")
                .build();

        return HttpUtils.buildURL(searchLink, queryParams);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> parse(final String data) {
        JSONMovieParser movieParser = new JSONMovieParser();
        JsonParser jsonParser = new JsonParser();

        return jsonParser.parse(data)
                .getAsJsonObject()
                .entrySet()
                .stream()
                .map(e -> e.getValue().getAsJsonArray())
                .flatMap(a -> StreamSupport.stream(a.spliterator(), false))
                .map(e -> movieParser.parse(
                        e.getAsJsonObject(),
                        t -> t.get("title"),
                        t -> t.get("description"),
                        t -> t.get("id")
                ))
                .collect(Collectors.toList());
    }

    private static final class JSONMovieParser implements MovieParser<JsonObject, JsonElement, JsonElement, JsonElement> {
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
