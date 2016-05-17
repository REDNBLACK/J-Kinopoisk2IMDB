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

public final class IMDBHTMLExchangeStrategy extends AbstractExchangeStrategy {
    public IMDBHTMLExchangeStrategy() {
        super(MovieParsers.ofSourceType(DocumentSourceType.IMDB_HTML));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection.Request buildRequest(@NonNull final Movie movie) {
        val searchLink = "http://www.imdb.com/find";
        val queryParams = new ImmutableMap.Builder<String, String>()
                .put("q", movie.getTitle())
                .put("s", "tt")
                //.put("exact", "true")
                .put("ref", "fn_tt_ex")
                .build();

        return HttpConnection.connect(HttpUtils.buildURL(searchLink, queryParams)).request();
    }
}
