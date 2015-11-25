package org.f0w.k2i.core.exchange;

import com.google.inject.Inject;
import org.f0w.k2i.core.configuration.Configuration;
import org.f0w.k2i.core.net.HttpClient;
import org.f0w.k2i.core.net.HttpRequest;
import org.f0w.k2i.core.entities.Movie;
import org.f0w.k2i.core.net.Request;
import org.f0w.k2i.core.net.Response;

import org.jsoup.Jsoup;
import java.nio.charset.StandardCharsets;

public class MovieAuthStringFetcher {
    @Inject
    private Configuration config;

    public String handle(Movie movie) {
        Response response = sendRequest(movie);

        return handleResponse(response);
    }

    protected Response sendRequest(Movie movie) {
        Request request = HttpRequest.builder()
                .setUrl("http://www.imdb.com/title/" + movie.getImdbId())
                .setUserAgent(config.get("user_agent"))
                .addCookie("id", config.get("auth"))
                .build()
        ;

        return new HttpClient().sendRequest(request).getResponse();
    }

    protected String handleResponse(Response data) {
        return Jsoup.parse(data.toString(), StandardCharsets.UTF_8.name())
                .select("[data-auth]")
                .first()
                .attr("data-auth")
        ;
    }
}
