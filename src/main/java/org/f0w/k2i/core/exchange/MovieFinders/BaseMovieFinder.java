package org.f0w.k2i.core.exchange.MovieFinders;

import org.f0w.k2i.core.Components.Configuration;
import org.f0w.k2i.core.net.HttpRequest;
import org.f0w.k2i.core.entities.Movie;

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

        return handleResponse(response);
    }

    protected abstract URL buildSearchQuery(Movie movie);

    protected abstract List<Movie> parseSearchResult(final String result);

    protected String sendRequest(Movie movie) {
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

    protected List<Movie> handleResponse(String data) {
        return parseSearchResult(data);
    }
}
