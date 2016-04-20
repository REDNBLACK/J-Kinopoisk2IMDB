package org.f0w.k2i.core.exchange;

import org.f0w.k2i.core.model.entity.Movie;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;

import java.util.Map;
import java.util.Optional;

/**
 * Abstract class used by classes
 * returning HTTP status code in JSON response body, instead of headers.
 */
abstract class IMDBJSONExchange implements Exchangeable<Movie, Long> {
    protected Connection.Response response;

    @Override
    public Connection.Response getRawResponse() {
        return response;
    }

    /**
     * Returns HTTP status code parsed from JSON response
     *
     * @return HTTP status code or 0 if an error occured
     */
    @Override
    public Long getProcessedResponse() {
        try {
            Map data = (Map) new JSONParser().parse(response.body());

            return Optional.ofNullable((Long) data.get("status")).orElse(0L);
        } catch (NullPointerException | ClassCastException | ParseException ignore) {
            return 0L;
        }
    }
}
