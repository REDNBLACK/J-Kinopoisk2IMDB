package org.f0w.k2i.core.exchange;

import com.google.common.collect.ImmutableMap;
import org.f0w.k2i.core.Components.Configuration;
import org.f0w.k2i.core.net.HttpRequest;
import org.f0w.k2i.core.entities.Movie;

import java.io.IOException;
import java.util.Map;

public class MovieRatingChanger {
    private HttpRequest request;
    private Configuration config;
    private MovieAuthStringFetcher fetcher;

    public MovieRatingChanger(HttpRequest request, Configuration config, MovieAuthStringFetcher fetcher) {
        this.request = request;
        this.config = config;
        this.fetcher = fetcher;
    }

    public int handle(Movie movie) {
        String movieAuth = fetcher.fetch(movie);
        int response = sendRequest(movie, movieAuth);

        return handleResponse(response);
    }

    protected int sendRequest(Movie movie, String movieAuth) {
        int statusCode = 500;

        try {
            Map<String, String> postData = new ImmutableMap.Builder<String, String>()
                    .put("tconst", movie.getImdbId())                 // ID фильма
                    .put("rating", String.valueOf(movie.getRating())) // Рейтинг
                    .put("auth", movieAuth)                           // ID авторизации фильма
                    .put("pageId", movie.getImdbId())                 // ID страницы (совпадает с ID фильма)
                    .put("tracking_tag", "title-maindetails")         // Тэг для трекинга не меняется
                    .put("pageType", "title")                         // Реферер не меняется
                    .put("subpageType", "main")                       // Тип страницы не меняется
                    .build()
            ;

            request.createRequest("http://www.imdb.com/ratings/_ajax/title");
            request.setPOSTData(postData);
            request.setCookies(ImmutableMap.of("id", config.get("auth")));
            request.setUserAgent(config.get("user_agent"));

            statusCode = request.getStatusCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return statusCode;
    }

    protected int handleResponse(int statusCode) {
        return statusCode;
    }
}