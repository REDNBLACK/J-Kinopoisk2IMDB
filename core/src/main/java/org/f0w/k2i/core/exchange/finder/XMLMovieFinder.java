package org.f0w.k2i.core.exchange.finder;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

final class XMLMovieFinder extends AbstractMovieFinder {
    private static final String SEARCH_LINK = "http://www.imdb.com/xml/find?";

    public XMLMovieFinder(Config config) {
        super(config, SEARCH_LINK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> buildSearchQuery(Movie movie) {
        return new ImmutableMap.Builder<String, String>()
                .put("q", movie.getTitle())
                .put("tt", "on")
                .put("nr", "1")
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> parseSearchResult(String result) {
        Document document = Jsoup.parse(result);

        return document.getElementsByTag("ImdbEntity")
                .stream()
                .map(e -> new XMLMovieParser(e).parse(
                        Element::ownText,
                        t -> t.getElementsByTag("Description").first(),
                        t -> t.attr("id")
                ))
                .collect(Collectors.toList());
    }

    private static final class XMLMovieParser extends MovieParser<Element, String, Element, String> {
        public XMLMovieParser(Element root) {
            super(root);
        }

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
