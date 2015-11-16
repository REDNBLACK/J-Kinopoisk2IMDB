package org.f0w.k2i.core.Requests;

import com.google.common.collect.ImmutableMap;
import org.f0w.k2i.core.Components.Configuration;
import org.f0w.k2i.core.Components.HttpRequest;
import org.f0w.k2i.core.Models.Movie;

import java.io.IOException;
import java.util.Map;

public class MovieWatchlistAssigner {
    private HttpRequest request;
    private Configuration config;

    public MovieWatchlistAssigner(HttpRequest request, Configuration config) {
        this.request = request;
        this.config = config;
    }

    public int handle(Movie movie) {
        return sendRequest(movie);
    }

    private int sendRequest(Movie movie) {
        int statusCode = 500;

        try {
            Map<String, String> postData = new ImmutableMap.Builder<String, String>()
                    .put("const", movie.getImdbId())    // ID фильма
                    .put("list_id", config.get("list")) // ID списка для добавления
                    .put("ref_tag", "title")            // Реферер не меняется
                    .build()
            ;

            request.createRequest("http://www.imdb.com/list/_ajax/edit");
            request.setPOSTData(postData);
            request.setCookies(ImmutableMap.of("id", config.get("auth")));
            request.setUserAgent(config.get("user_agent"));

            statusCode = request.getStatusCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return statusCode;
    }
}
