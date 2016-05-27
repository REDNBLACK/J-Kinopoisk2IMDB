package org.f0w.k2i.core.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.val;
import org.f0w.k2i.core.model.entity.Movie;

import java.util.function.Function;
import java.util.stream.Stream;

final class OMDBMovieParser extends AbstractJSONMovieParser<JsonObject> {
    @Override
    protected Stream<JsonObject> getStructureStream(final String data) {
        return Stream.of(new JsonParser().parse(data))
                .filter(JsonElement::isJsonObject)
                .map(JsonElement::getAsJsonObject)
                .filter(e -> !e.entrySet().isEmpty())
                .filter(e -> e.has("imdbID"));
    }

    @Override
    protected Function<JsonObject, Movie> getDataMapper() {
        return e -> new Movie(
                parseTitle(stringOrNull(e.get("Title"), JsonElement::getAsString)),
                parseYear(stringOrNull(e.get("Year"), JsonElement::getAsString)),
                parseType(e),
                null,
                parseIMDBId(stringOrNull(e.get("imdbID"), JsonElement::getAsString))
        );
    }

    private Movie.Type parseType(JsonObject element) {
        boolean isSeries = "series".equals(stringOrNull(element.get("Type"), JsonElement::getAsString));
        if (isSeries) {
            return Movie.Type.SERIES;
        }

        val genres = String.valueOf(stringOrNull(element.get("Genre"), JsonElement::getAsString));
        if (genres.contains("Documentary")) {
            return Movie.Type.DOCUMENTARY;
        } else if (genres.contains("Short")) {
            return Movie.Type.SHORT;
        }

        return Movie.Type.MOVIE;
    }
}
