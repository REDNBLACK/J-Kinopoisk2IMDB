package org.f0w.k2i.core.exchange.finder.strategy;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import lombok.val;
import org.f0w.k2i.core.DocumentSourceType;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.util.HttpUtils;
import org.f0w.k2i.core.parser.MovieParsers;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

public final class OMDBExchangeStrategy extends AbstractExchangeStrategy {
    public OMDBExchangeStrategy() {
        super(MovieParsers.ofSourceType(DocumentSourceType.OMDB));
    }

    @Override
    public Connection.Request buildRequest(@NonNull Movie movie) {
        val searchLink = "http://www.omdbapi.com";
        val queryParams = new ImmutableMap.Builder<String, String>()
                .put("t", movie.getTitle())
                .put("plot", "short")
                .put("r", "json")
                .build();

        return HttpConnection.connect(HttpUtils.buildURL(searchLink, queryParams))
                .ignoreContentType(true)
                .request();
    }
}
