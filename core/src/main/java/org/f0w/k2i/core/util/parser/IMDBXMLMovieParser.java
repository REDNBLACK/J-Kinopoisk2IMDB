package org.f0w.k2i.core.util.parser;

import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

final class IMDBXMLMovieParser extends AbstractMovieParser {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> parse(final String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Document document = Jsoup.parse(data);

        return document.getElementsByTag("ImdbEntity")
                .stream()
                .map(e -> new Movie(
                        parseTitle(e.ownText()),
                        parseYear(stringOrNull(e.getElementsByTag("Description").first())),
                        parseType(stringOrNull(e.getElementsByTag("Description").first())),
                        null,
                        parseIMDBId(e.attr("id"))
                ))
                .collect(Collectors.toList());
    }

    private String stringOrNull(Element element) {
        return Optional.ofNullable(element)
                .map(Element::text)
                .orElse(null);
    }
}
