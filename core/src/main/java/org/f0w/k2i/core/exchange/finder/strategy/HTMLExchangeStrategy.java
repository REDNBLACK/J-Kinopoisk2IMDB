package org.f0w.k2i.core.exchange.finder.strategy;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import lombok.val;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.parser.HTMLMovieParser;
import org.f0w.k2i.core.util.HttpUtils;

import java.net.URL;

public final class HTMLExchangeStrategy extends AbstractExchangeStrategy {
    public HTMLExchangeStrategy() {
        super(new HTMLMovieParser());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL buildSearchURL(@NonNull final Movie movie) {
        val searchLink = "http://www.imdb.com/find";
        val queryParams = new ImmutableMap.Builder<String, String>()
                .put("q", movie.getTitle())
                .put("s", "tt")
                //.put("exact", "true")
                .put("ref", "fn_tt_ex")
                .build();

        return HttpUtils.buildURL(searchLink, queryParams);
    }
}
