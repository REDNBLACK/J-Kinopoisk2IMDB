package org.f0w.k2i.core.exchange;

import org.f0w.k2i.core.model.entity.Movie;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;

import java.util.Map;
import java.util.Optional;

abstract class POSTMovieExchange implements Exchangeable<Movie, Long> {
    protected Connection.Response response;

    @Override
    public Connection.Response getRawResponse() {
        return response;
    }

    @Override
    public Long getProcessedResponse() {
        try {
            Map data = (Map) new JSONParser().parse(response.body());

            return Optional.ofNullable((Long) data.getOrDefault("status", 0L)).orElse(0L);
        } catch (NullPointerException|ClassCastException|ParseException ignore) {
            return 0L;
        }
    }
}
