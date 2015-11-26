package org.f0w.k2i.core.exchange.MovieFinders;

import org.f0w.k2i.core.configuration.Configuration;
import org.f0w.k2i.core.entities.Movie;
import org.f0w.k2i.core.net.*;

import java.net.URL;
import java.util.List;

abstract class BaseMovieFinder implements MovieFinder {
    protected Configuration config;
    protected Response response;

    public BaseMovieFinder(Configuration config) {
        this.config = config;
    }

    protected abstract URL buildSearchQuery(Movie movie);

    protected abstract List<Movie> parseSearchResult(final String result);

    public void sendRequest(Movie movie) {
        Request request = HttpRequest.builder()
                .setUrl(buildSearchQuery(movie))
                .setUserAgent(config.get("user_agent"))
                .build()
        ;

        response = new HttpClient().sendRequest(request).getResponse();
    }

    @Override
    public Response getRawResponse() {
        return response;
    }

    @Override
    public List<Movie> getProcessedResponse() {
        return parseSearchResult(response.toString());
    }
}
