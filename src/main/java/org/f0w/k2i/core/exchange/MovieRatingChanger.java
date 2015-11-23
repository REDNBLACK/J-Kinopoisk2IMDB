package org.f0w.k2i.core.exchange;

import com.google.common.collect.ImmutableMap;

import org.f0w.k2i.core.configuration.Configuration;
import org.f0w.k2i.core.entities.Movie;
import org.f0w.k2i.core.net.*;

import java.util.Map;

public class MovieRatingChanger {
    private Configuration config;
    private MovieAuthStringFetcher fetcher;

    public MovieRatingChanger(Configuration config, MovieAuthStringFetcher fetcher) {
        this.config = config;
        this.fetcher = fetcher;
    }

    public int handle(Movie movie) {
        String movieAuth = fetcher.fetch(movie);
        Response response = sendRequest(movie, movieAuth);

        return handleResponse(response);
    }

    protected Response sendRequest(Movie movie, String movieAuth) {
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

        Request request = HttpRequest.builder()
                .setUrl("http://www.imdb.com/ratings/_ajax/title")
                .setMethod(HttpMethod.POST)
                .setUserAgent(config.get("user_agent"))
                .addCookie("id", config.get("auth"))
                .addPOSTData(postData)
                .build()
        ;

        return new HttpClient().sendRequest(request).getResponse();
    }

    protected int handleResponse(Response response) {
        return response.getStatusCode();
    }
}
