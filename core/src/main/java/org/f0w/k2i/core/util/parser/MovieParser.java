package org.f0w.k2i.core.util.parser;

import org.f0w.k2i.core.model.entity.Movie;

import java.util.List;

/**
 * Interface for parsing of Movie
 */
@FunctionalInterface
public interface MovieParser {
    /**
     *
     * @param data Data to parse
     * @return List of parsed movies
     */
    List<Movie> parse(final String data);
}
