package org.f0w.k2i.core.exchange;

import com.typesafe.config.Config;
import lombok.NonNull;
import lombok.val;
import org.f0w.k2i.core.exchange.processor.ResponseProcessor;
import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

/**
 * Parses authorisation string used for setting a Movie rating.
 */
public final class MovieAuthStringFetcher implements Exchangeable<Movie, String> {
    private final Config config;

    public MovieAuthStringFetcher(@NonNull Config config) {
        this.config = config;
    }

    /**
     * Opens movie page using {@link Movie#imdbId}
     *
     * @param movie Movie which page should be opened
     * @throws IOException If an I/O error occurs
     */
    @Override
    public ExchangeObject<String> prepare(@NonNull Movie movie) throws IOException {
        val moviePageLink = new URL("http://www.imdb.com/title/" + movie.getImdbId());

        val request = HttpConnection.connect(moviePageLink)
                .userAgent(config.getString("user_agent"))
                .timeout(config.getInt("timeout"))
                .cookie("id", config.getString("auth"))
                .request();

        return new ExchangeObject<>(request, getResponseProcessor());
    }

    /**
     * @return Authorization string or null if response is empty
     */
    private ResponseProcessor<String> getResponseProcessor() {
        return response -> Optional.ofNullable(Jsoup.parse(response.body()))
                .map(e -> e.select("[data-auth]").first())
                .map(e -> e.attr("data-auth"))
                .orElse(null);
    }
}
