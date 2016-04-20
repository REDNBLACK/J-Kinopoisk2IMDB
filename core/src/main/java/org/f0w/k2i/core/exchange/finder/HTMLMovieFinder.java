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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

final class HTMLMovieFinder extends AbstractMovieFinder {
    private static final String SEARCH_LINK = "http://www.imdb.com/find?";

    public HTMLMovieFinder(Config config) {
        super(config, SEARCH_LINK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> buildSearchQuery(Movie movie) {
        return new ImmutableMap.Builder<String, String>()
                .put("q", movie.getTitle())
                .put("s", "tt")
                //.put("exact", "true")
                .put("ref", "fn_tt_ex")
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> parseSearchResult(String result) {
        Document document = Jsoup.parse(result);

        return document.select("table.findList tr td.result_text")
                .stream()
                .map(e -> new HTMLMovieParser(e).parse(
                        t -> t,
                        Element::text,
                        t -> t.getElementsByTag("a").first()
                ))
                .collect(Collectors.toList());
    }

    private static final class HTMLMovieParser extends MovieParser<Element, Element, String, Element> {
        public HTMLMovieParser(Element root) {
            super(root);
        }

        @Override
        public String prepareTitle(Element element) {
            String elementText = element.text();

            if (elementText.contains("(TV Episode)")) {
                String title = elementText;

                Matcher m = Pattern.compile("\\(TV Episode\\)\\s*-(.+?)(?=\\(.*\\)\\s*\\(TV Series\\))")
                        .matcher(elementText);
                while (m.find()) {
                    title = m.group(1);
                }

                return title;
            }

            return Optional.ofNullable(element.getElementsByTag("a").first())
                    .map(Element::text)
                    .orElse(null);
        }

        @Override
        public String prepareYear(String element) {
            String preparedYear = element;

            Matcher m = Pattern.compile("\\(([0-9]{4})\\)").matcher(element);
            while (m.find()) {
                preparedYear = m.group(1);
            }

            return preparedYear;
        }

        @Override
        public String prepareImdbId(Element element) {
            return Optional.ofNullable(element)
                    .map(e -> e.attr("href"))
                    .map(e -> e.split("/"))
                    .map(e -> e.length < 2 ? null : e[2])
                    .orElse(null);
        }
    }
}
