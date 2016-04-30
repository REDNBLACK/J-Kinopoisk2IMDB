package org.f0w.k2i.core.exchange;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Optional;

/**
 * Parses authorisation string used for setting a Movie rating.
 */
public final class MovieAuthStringFetcher extends AbstractExchangeable<Movie, String> {
    private final Config config;

    @Inject
    public MovieAuthStringFetcher(Config config) {
        this.config = config;
    }

    /**
     * Opens movie page using {@link Movie#imdbId}
     *
     * @param movie Movie which page should be opened
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void sendRequest(Movie movie) throws IOException {
        final String moviePageLink = "http://www.imdb.com/title/";

        Connection request = Jsoup.connect(moviePageLink + movie.getImdbId())
                .userAgent(config.getString("user_agent"))
                .timeout(config.getInt("timeout"))
                .cookie("id", config.getString("auth"));

        setResponse(request.execute());
    }

    /**
     * Parses Movie authorization string
     *
     * @return Authorization string or null if response is empty
     */
    @Override
    public String getProcessedResponse() {
        return Optional.ofNullable(Jsoup.parse(response.body()))
                .map(e -> e.select("[data-auth]").first())
                .map(e -> e.attr("data-auth"))
                .orElse(null);
    }
}
