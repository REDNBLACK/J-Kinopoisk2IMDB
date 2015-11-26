package org.f0w.k2i.core.exchange;

import org.f0w.k2i.core.configuration.Configuration;
import org.f0w.k2i.core.entities.Movie;
import org.f0w.k2i.core.net.*;

import com.google.inject.Inject;
import org.jsoup.Jsoup;

import java.nio.charset.StandardCharsets;

public class MovieAuthStringFetcher implements Exchangeable<Movie, String> {
    private Configuration config;
    private Response response;

    @Inject
    public MovieAuthStringFetcher(Configuration config) {
        this.config = config;
    }

    @Override
    public void sendRequest(Movie movie) {
        Request request = HttpRequest.builder()
                .setUrl("http://www.imdb.com/title/" + movie.getImdbId())
                .setUserAgent(config.get("user_agent"))
                .addCookie("id", config.get("auth"))
                .build()
        ;

        response = new HttpClient().sendRequest(request).getResponse();
    }

    @Override
    public Response getRawResponse() {
        return response;
    }

    @Override
    public String getProcessedResponse() {
        return Jsoup.parse(response.toString(), StandardCharsets.UTF_8.name())
                .select("[data-auth]")
                .first()
                .attr("data-auth")
        ;
    }
}
