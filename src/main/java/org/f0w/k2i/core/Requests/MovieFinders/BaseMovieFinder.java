package org.f0w.k2i.core.Requests.MovieFinders;

import org.f0w.k2i.core.Components.Configuration;
import org.f0w.k2i.core.Components.HttpRequest;
import org.f0w.k2i.core.Models.Movie;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public abstract class BaseMovieFinder implements MovieFinder {
    protected HttpRequest request;
    protected Configuration config;

    public BaseMovieFinder(HttpRequest request, Configuration config) {
        this.request = request;
        this.config = config;
    }

    public List<Movie> find(Movie movie) {
        String response = sendRequest(movie);

        return parseResponse(response);
    }

    protected abstract URL buildSearchQuery(Movie movie);

    protected abstract List<Movie> parseSearchResult(final String result);

    private String sendRequest(Movie movie) {
        String response = null;

        try {
            request.createRequest(buildSearchQuery(movie));
            request.setUserAgent(config.get("user_agent"));

            response = request.getResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private List<Movie> parseResponse(String data) {
        return parseSearchResult(data);
    }
}
