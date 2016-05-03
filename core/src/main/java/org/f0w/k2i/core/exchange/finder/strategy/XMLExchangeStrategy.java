package org.f0w.k2i.core.exchange.finder.strategy;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import lombok.val;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.util.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class XMLExchangeStrategy implements ExchangeStrategy {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> parseSearchResult(@NonNull final String data) {
        Document document = Jsoup.parse(data);
        XMLMovieParser parser = new XMLMovieParser();

        return document.getElementsByTag("ImdbEntity")
                .stream()
                .map(e -> parser.parse(
                        e,
                        Element::ownText,
                        t -> t.getElementsByTag("Description").first(),
                        t -> t.attr("id")
                ))
                .collect(Collectors.toList());
    }

    private static final class XMLMovieParser implements MovieParser<Element, String, Element, String> {
        @Override
        public String prepareTitle(String element) {
            return element;
        }

        @Override
        public String prepareYear(Element element) {
            return Optional.ofNullable(element)
                    .map(Element::text)
                    .orElse(null);
        }

        @Override
        public String prepareImdbId(String element) {
            return element;
        }
    }
}
