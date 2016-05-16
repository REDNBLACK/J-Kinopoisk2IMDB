package org.f0w.k2i.core.util.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.f0w.k2i.core.model.entity.Movie;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

final class IMDBJSONMovieParser extends AbstractMovieParser {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> parse(final String data) {
        if (data == null || "".equals(data)) {
            return Collections.emptyList();
        }

        JsonParser jsonParser = new JsonParser();

        return jsonParser.parse(data)
                .getAsJsonObject()
                .entrySet()
                .stream()
                .map(e -> e.getValue().getAsJsonArray())
                .flatMap(a -> StreamSupport.stream(a.spliterator(), false))
                .map(JsonElement::getAsJsonObject)
                .map(e -> new Movie(
                        parseTitle(stringOrNull(e.get("title"))),
                        parseYear(stringOrNull(e.get("description"))),
                        parseType(stringOrNull(e.get("description"))),
                        null,
                        parseIMDBId(stringOrNull(e.get("id")))
                ))
                .collect(Collectors.toList());
    }

    private String stringOrNull(JsonElement element) {
        return Optional.ofNullable(element)
                .map(JsonElement::getAsString)
                .orElse(null);
    }
}
