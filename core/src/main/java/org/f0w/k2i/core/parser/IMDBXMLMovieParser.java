package org.f0w.k2i.core.parser;

import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.function.Function;
import java.util.stream.Stream;

final class IMDBXMLMovieParser extends AbstractMovieParser<Element> {
    /**
     * {@inheritDoc}
     */
    @Override
    protected Stream<Element> getStructureStream(final String data) {
        return Jsoup.parse(data)
                .getElementsByTag("ImdbEntity")
                .stream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Function<Element, Movie> getDataMapper() {
        return e -> new Movie(
                parseTitle(e.ownText()),
                parseYear(stringOrNull(e.getElementsByTag("Description").first(), Element::text)),
                parseType(stringOrNull(e.getElementsByTag("Description").first(), Element::text)),
                null,
                parseIMDBId(e.attr("id"))
        );
    }
}
