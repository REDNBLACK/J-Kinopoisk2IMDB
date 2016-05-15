package org.f0w.k2i.core.exchange.finder.strategy;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import lombok.val;
import org.f0w.k2i.core.DocumentSourceType;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.util.HttpUtils;
import org.f0w.k2i.core.util.parser.MovieParsers;

import java.net.URL;

public final class XMLExchangeStrategy extends AbstractExchangeStrategy {
    public XMLExchangeStrategy() {
        super(MovieParsers.ofSourceType(DocumentSourceType.XML));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL buildSearchURL(@NonNull final Movie movie) {
        val searchLink = "http://www.imdb.com/xml/find";
        val queryParams = new ImmutableMap.Builder<String, String>()
                .put("q", movie.getTitle())
                .put("tt", "on")
                .put("nr", "1")
                .build();

        return HttpUtils.buildURL(searchLink, queryParams);
    }
}
