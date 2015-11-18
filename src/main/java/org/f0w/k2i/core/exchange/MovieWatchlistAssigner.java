package org.f0w.k2i.core.exchange;

import com.google.common.collect.ImmutableMap;

import org.f0w.k2i.core.configuration.Configuration;
import org.f0w.k2i.core.net.HttpRequest;
import org.f0w.k2i.core.entities.Movie;
import org.f0w.k2i.core.net.Response;

import java.util.Map;

public class MovieWatchlistAssigner {
    private Configuration config;

    public MovieWatchlistAssigner(Configuration config) {
        this.config = config;
    }

    public int handle(Movie movie) {
        Response response = sendRequest(movie);

        return handleResponse(response);
    }

    private Response sendRequest(Movie movie) {
        Map<String, String> postData = new ImmutableMap.Builder<String, String>()
                .put("const", movie.getImdbId())    // ID фильма
                .put("list_id", config.get("list")) // ID списка для добавления
                .put("ref_tag", "title")            // Реферер не меняется
                .build()
        ;

        return new HttpRequest.Builder()
                .setUrl("http://www.imdb.com/list/_ajax/edit")
                .setUserAgent(config.get("user_agent"))
                .addCookie("id", config.get("auth"))
                .addPOSTData(postData)
                .build()
                .getResponse()
        ;
    }

    protected int handleResponse(Response response) {
        return response.getStatusCode();
    }
}
