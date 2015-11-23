package org.f0w.k2i.core.exchange.MovieFinders;

import org.f0w.k2i.core.configuration.Configuration;
import org.f0w.k2i.core.entities.Movie;
import org.f0w.k2i.core.net.HttpClient;
import org.f0w.k2i.core.net.HttpRequest;
import org.f0w.k2i.core.net.Request;
import org.f0w.k2i.core.net.Response;

import java.net.URL;
import java.util.List;

abstract class BaseMovieFinder implements MovieFinder {
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
        Request request = HttpRequest.builder()
                .setUrl(buildSearchQuery(movie))
                .setUserAgent(config.get("user_agent"))
                .build()
        ;

        System.out.println(config.get("user_agent"));
        System.out.println(request);

        return new HttpClient().sendRequest(request).getResponse();
    }

    protected List<Movie> handleResponse(Response response) {
        return parseSearchResult(response.toString());
    }
}
