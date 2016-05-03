package org.f0w.k2i.core.exchange;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.Optional;

/**
 * Abstract class used by classes
 * returning HTTP status code in JSON response body, instead of headers.
 */
public abstract class JSONPostExchangeable<IN> extends AbstractExchangeable<IN, Integer> {
    /**
     * Returns HTTP status code parsed from JSON response
     *
     * @return HTTP status code or 0 if an error occurred
     */
    @Override
    public Integer getProcessedResponse() {
        try {
            return Optional.ofNullable(new JsonParser().parse(getResponseBody()))
                    .map(e -> e.isJsonObject() ? e.getAsJsonObject() : null)
                    .map(e -> e.get("status"))
                    .map(JsonElement::getAsInt)
                    .orElse(0);
        } catch (JsonParseException ignore) {
            return 0;
        }
    }
}
