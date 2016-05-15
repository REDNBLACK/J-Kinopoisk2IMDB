package org.f0w.k2i.core.exchange.finder;

import com.typesafe.config.Config;
import lombok.NonNull;
import lombok.val;
import org.f0w.k2i.core.DocumentSourceType;
import org.f0w.k2i.core.exchange.ExchangeObject;
import org.f0w.k2i.core.exchange.finder.strategy.ExchangeStrategy;
import org.f0w.k2i.core.exchange.processor.ResponseProcessor;
import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.helper.HttpConnection;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

final class BasicMovieFinder implements MovieFinder {
    @NonNull
    private final DocumentSourceType documentSourceType;

    @NonNull
    private final ExchangeStrategy exchangeStrategy;

    @NonNull
    private final Config config;

    public BasicMovieFinder(DocumentSourceType documentSourceType, ExchangeStrategy exchangeStrategy, Config config) {
        this.documentSourceType = documentSourceType;
        this.exchangeStrategy = exchangeStrategy;
        this.config = config;
    }

    /**
     * Open movie search page using {@link Movie#year} and {@link Movie#title}.
     *
     * @param movie Movie to use
     * @throws IOException If an I/O error occurs
     */
    @Override
    public ExchangeObject<Deque<Movie>> prepare(@NonNull Movie movie) throws IOException {
        val movieSearchLink = exchangeStrategy.buildSearchURL(movie);

        val request = HttpConnection.connect(movieSearchLink)
                .userAgent(config.getString("user_agent"))
                .timeout(config.getInt("timeout"))
                .request();

        return new ExchangeObject<>(request, getResponseProcessor());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentSourceType getDocumentSourceType() {
        return documentSourceType;
    }

    /**
     * @return Deque of parsed movies using current {@link ExchangeStrategy}
     */
    private ResponseProcessor<Deque<Movie>> getResponseProcessor() {
        return response -> new ArrayDeque<>(exchangeStrategy.parseSearchResult(response));
    }
}
