package org.f0w.k2i.core.exchange.finder.strategy;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import lombok.val;
import org.f0w.k2i.core.DocumentSourceType;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.util.HttpUtils;
import org.f0w.k2i.core.util.parser.MovieParsers;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

public final class IMDBJSONExchangeStrategy extends AbstractExchangeStrategy {
    public IMDBJSONExchangeStrategy() {
        super(MovieParsers.ofSourceType(DocumentSourceType.IMDB_JSON));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection.Request buildRequest(@NonNull final Movie movie) {
        val searchLink = "http://www.imdb.com/xml/find";
        val queryParams = new ImmutableMap.Builder<String, String>()
                .put("q", movie.getTitle())
                .put("tt", "on")
                .put("nr", "1")
                .put("json", "1")
                .build();

        return HttpConnection.connect(HttpUtils.buildURL(searchLink, queryParams)).request();
    }
}
