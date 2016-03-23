package org.f0w.k2i.core.exchange;

import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import com.google.inject.Inject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MovieAuthStringFetcher implements Exchangeable<Movie, String> {
    @Inject
    private Config config;

    private Connection.Response response;

    @Override
    public void sendRequest(Movie movie) throws IOException {
        final String moviePageLink = "http://www.imdb.com/title/";

        Connection request = Jsoup.connect(moviePageLink + movie.getImdbId())
                .userAgent(config.getString("user_agent"))
                .timeout(config.getInt("timeout"))
                .cookie("id", config.getString("auth"));

        response = request.execute();
    }

    @Override
    public Connection.Response getRawResponse() {
        return response;
    }

    @Override
    public String getProcessedResponse() {
        return Jsoup.parse(response.body(), StandardCharsets.UTF_8.name())
                .select("[data-auth]")
                .first()
                .attr("data-auth");
    }
}
