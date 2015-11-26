package org.f0w.k2i.core.exchange;

import org.f0w.k2i.core.configuration.Configuration;
import org.f0w.k2i.core.entities.Movie;
import org.f0w.k2i.core.net.*;

import com.google.common.collect.ImmutableMap;

import javax.inject.Inject;
import java.util.Map;

public class MovieRatingChanger implements Exchangeable<Movie, Response> {
    private Configuration config;
    private MovieAuthStringFetcher fetcher;
    private Response response;

    @Inject
    public MovieRatingChanger(Configuration config, MovieAuthStringFetcher fetcher) {
        this.config = config;
        this.fetcher = fetcher;
    }

    @Override
    public void sendRequest(Movie movie) {
        fetcher.sendRequest(movie);

        Map<String, String> postData = new ImmutableMap.Builder<String, String>()
                .put("tconst", movie.getImdbId())                 // ID фильма
                .put("rating", String.valueOf(movie.getRating())) // Рейтинг
                .put("auth", fetcher.getProcessedResponse())      // ID авторизации фильма
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

        response = new HttpClient().sendRequest(request).getResponse();
    }

    @Override
    public Response getRawResponse() {
        return response;
    }

    @Override
    public Response getProcessedResponse() {
        return getRawResponse();
    }
}
