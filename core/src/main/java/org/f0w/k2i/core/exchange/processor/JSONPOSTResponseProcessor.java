package org.f0w.k2i.core.exchange.processor;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.jsoup.Connection;

import java.util.Optional;

/**
 * Processor for post request - json response status in body of document
 */
public class JSONPOSTResponseProcessor implements ResponseProcessor<Integer> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Integer process(Connection.Response response) {
        int statusCode = 0;
        try {
            statusCode = response.statusCode();
            return Optional.ofNullable(new JsonParser().parse(response.body()))
                    .map(e -> e.isJsonObject() ? e.getAsJsonObject() : null)
                    .map(e -> e.get("status"))
                    .map(JsonElement::getAsInt)
                    .orElse(0);
        } catch (JsonParseException ignore) {
            return statusCode;
        }
    }
}
