package org.f0w.k2i.core.util.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.val;
import org.f0w.k2i.core.model.entity.Movie;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

final class JSONMovieParser extends AbstractMovieParser {
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
                        parseTitle(e.get("title")),
                        parseYear(e.get("description")),
                        parseType(e.get("description")),
                        null,
                        parseIMDBId(e.get("id"))
                ))
                .collect(Collectors.toList());
    }

    protected String parseTitle(JsonElement element) {
        return super.parseTitle(stringOrNull(element));
    }

    protected int parseYear(JsonElement element) {
        return super.parseYear(stringOrNull(element));
    }

    protected Movie.Type parseType(JsonElement element) {
        val stringValue = Optional.ofNullable(element)
                .map(JsonElement::getAsString)
                .orElse("");

        return super.parseType(stringValue);
    }

    protected String parseIMDBId(JsonElement element) {
        return super.parseIMDBId(stringOrNull(element));
    }

    private String stringOrNull(JsonElement element) {
        return Optional.ofNullable(element)
                .map(JsonElement::getAsString)
                .orElse(null);
    }
}
