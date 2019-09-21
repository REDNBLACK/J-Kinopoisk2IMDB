package org.f0w.k2i.core.parser;

import org.f0w.k2i.core.DocumentSourceType;

/**
 * {@link MovieParser} factory
 */
public final class MovieParsers {
    private MovieParsers() {
    }

    /**
     * Returns instance of MovieParser based on {@link DocumentSourceType}.
     *
     * @param documentSourceType Which one to use
     * @return MovieParser
     */
    public static MovieParser ofSourceType(DocumentSourceType documentSourceType) {
        switch (documentSourceType) {
//            case IMDB_XML: return new IMDBXMLMovieParser();
//            case IMDB_JSON: return new IMDBJSONMovieParser();
            case IMDB_HTML: return new IMDBHTMLMovieParser();
            case OMDB: return new OMDBMovieParser();
            default: throw new IllegalArgumentException("Unsupported format!");
        }
    }

    /**
     * Returns MovieParser for Kinopoisk files.
     * @return Kinopoisk file MovieParser
     */
    public static MovieParser fileParser() {
        return new KinopoiskFileMovieParser();
    }
}
