package org.f0w.k2i.core.exchange.MovieFinders;

import org.f0w.k2i.core.configuration.Configuration;
import org.f0w.k2i.core.entities.Movie;
import org.f0w.k2i.core.net.HttpRequest;
import org.f0w.k2i.core.net.Response;

import java.net.URL;
import java.util.List;

public abstract class BaseMovieFinder implements MovieFinder {
    protected Configuration config;

    public BaseMovieFinder(Configuration config) {
        this.config = config;
    }

    public List<Movie> find(Movie movie) {
        Response response = sendRequest(movie);

        return handleResponse(response);
    }

    protected abstract URL buildSearchQuery(Movie movie);

    protected abstract List<Movie> parseSearchResult(final String result);

    protected Response sendRequest(Movie movie) {
        return new HttpRequest.Builder()
                .setUrl(buildSearchQuery(movie))
                .setUserAgent(config.get("user_agent"))
                .build()
                .getResponse()
        ;
    }

    protected List<Movie> handleResponse(Response response) {
        return parseSearchResult(response.toString());
    }
}
