package org.f0w.k2i.core.exchange.finder.strategy;

import com.google.common.collect.ImmutableMap;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.util.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public final class HTMLExchangeStrategy implements ExchangeStrategy {
    /**
     * {@inheritDoc}
     */
    @Override
    public URL buildSearchURL(final Movie movie) {
        requireNonNull(movie);

        String searchLink = "http://www.imdb.com/find";
        Map<String, String> queryParams = new ImmutableMap.Builder<String, String>()
                .put("q", movie.getTitle())
                .put("s", "tt")
                //.put("exact", "true")
                .put("ref", "fn_tt_ex")
                .build();

        return HttpUtils.buildURL(searchLink, queryParams);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> parseSearchResult(final String data) {
        Document document = Jsoup.parse(requireNonNull(data));
        HTMLMovieParser parser = new HTMLMovieParser();

        return document.select("table.findList tr td.result_text")
                .stream()
                .map(e -> parser.parse(
                        e,
                        t -> t,
                        Element::text,
                        t -> t.getElementsByTag("a").first()
                ))
                .collect(Collectors.toList());
    }

    private static final class HTMLMovieParser implements MovieParser<Element, Element, String, Element> {
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
