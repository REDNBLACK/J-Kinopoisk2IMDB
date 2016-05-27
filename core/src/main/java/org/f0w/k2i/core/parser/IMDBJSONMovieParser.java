package org.f0w.k2i.core.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.f0w.k2i.core.model.entity.Movie;

import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

final class IMDBJSONMovieParser extends AbstractJSONMovieParser<JsonObject> {
    /**
     * {@inheritDoc}
     */
    @Override
    protected Stream<JsonObject> getStructureStream(String data) {
        return new JsonParser().parse(data)
                .getAsJsonObject()
                .entrySet()
                .stream()
                .map(e -> e.getValue().getAsJsonArray())
                .flatMap(a -> StreamSupport.stream(a.spliterator(), false))
                .map(JsonElement::getAsJsonObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Function<JsonObject, Movie> getDataMapper() {
        return e -> new Movie(
                parseTitle(stringOrNull(e.get("title"), JsonElement::getAsString)),
                parseYear(stringOrNull(e.get("description"), JsonElement::getAsString)),
                parseType(stringOrNull(e.get("description"), JsonElement::getAsString)),
                null,
                parseIMDBId(stringOrNull(e.get("id"), JsonElement::getAsString))
        );
    }
}
