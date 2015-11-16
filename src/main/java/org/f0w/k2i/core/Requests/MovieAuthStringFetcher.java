package org.f0w.k2i.core.Requests;

import com.google.common.collect.ImmutableMap;
import org.f0w.k2i.core.Components.HttpRequest;
import org.f0w.k2i.core.Components.Configuration;
import org.f0w.k2i.core.Models.Movie;
import org.jsoup.Jsoup;

import java.io.IOException;

public class MovieAuthStringFetcher {
    private HttpRequest request;
    private Configuration config;

    public MovieAuthStringFetcher(HttpRequest request, Configuration config) {
        this.request = request;
        this.config = config;
    }

    public String fetch(Movie movie) {
        String response = sendRequest(movie);

        return parseResponse(response);
    }

    private String sendRequest(Movie movie) {
        String response = null;

        try {
            request.createRequest("http://www.imdb.com/title/" + movie.getImdbId());
            request.setCookies(ImmutableMap.of("id", config.get("auth")));
            request.setUserAgent(config.get("user_agent"));

            response = request.getResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private String parseResponse(String data) {
        return Jsoup.parse(data, "UTF-8")
            .select("[data-auth]")
            .first()
            .attr("data-auth")
        ;
    }
}
