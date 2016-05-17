package org.f0w.k2i.core.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.val;
import org.f0w.k2i.core.model.entity.Movie;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

final class OMDBMovieParser extends AbstractMovieParser {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> parse(String data) {
        if (data == null || "".equals(data)) {
            return Collections.emptyList();
        }

        JsonParser jsonParser = new JsonParser();

        return Optional.of(jsonParser.parse(data))
                .map(JsonElement::getAsJsonObject)
                .filter(e -> !e.entrySet().isEmpty())
                .filter(e -> e.has("imdbID"))
                .map(e -> new Movie(
                        parseTitle(stringOrNull(e.get("Title"))),
                        parseYear(stringOrNull(e.get("Year"))),
                        parseType(e),
                        null,
                        parseIMDBId(stringOrNull(e.get("imdbID")))
                ))
                .map(Collections::singletonList)
                .orElseGet(Collections::emptyList);
    }

    protected Movie.Type parseType(JsonObject element) {
        boolean isSeries = "series".equals(stringOrNull(element.get("Type")));
        if (isSeries) {
            return Movie.Type.SERIES;
        }

        val genres = String.valueOf(stringOrNull(element.get("Genre")));
        if (genres.contains("Documentary")) {
            return Movie.Type.DOCUMENTARY;
        } else if (genres.contains("Short")) {
            return Movie.Type.SHORT;
        }

        return Movie.Type.MOVIE;
    }

    private String stringOrNull(JsonElement element) {
        return Optional.ofNullable(element)
                .map(JsonElement::getAsString)
                .orElse(null);
    }
}
