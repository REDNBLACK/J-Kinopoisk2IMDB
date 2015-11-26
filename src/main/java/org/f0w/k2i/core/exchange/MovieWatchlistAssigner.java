package org.f0w.k2i.core.exchange;

import org.f0w.k2i.core.configuration.Configuration;
import org.f0w.k2i.core.net.*;
import org.f0w.k2i.core.entities.Movie;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

import java.util.Map;

public class MovieWatchlistAssigner implements Exchangeable<Movie, Response> {
    private Configuration config;
    private Response response;

    @Inject
    public MovieWatchlistAssigner(Configuration config) {
        this.config = config;
    }

    @Override
    public void sendRequest(Movie movie) {
        Map<String, String> postData = new ImmutableMap.Builder<String, String>()
                .put("const", movie.getImdbId())    // ID фильма
                .put("list_id", config.get("list")) // ID списка для добавления
                .put("ref_tag", "title")            // Реферер не меняется
                .build()
        ;

        Request request = HttpRequest.builder()
                .setUrl("http://www.imdb.com/list/_ajax/edit")
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
