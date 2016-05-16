package org.f0w.k2i.core.util.parser;

import org.f0w.k2i.core.DocumentSourceType;

public final class MovieParsers {
    private MovieParsers() {
    }

    public static MovieParser ofSourceType(DocumentSourceType documentSourceType) {
        switch (documentSourceType) {
            case IMDB_XML: return new IMDBXMLMovieParser();
            case IMDB_JSON: return new IMDBJSONMovieParser();
            case IMDB_HTML: return new IMDBHTMLMovieParser();
            case OMDB: return new OMDBMovieParser();
            default: throw new IllegalArgumentException("Unsupported format!");
        }
    }

    public static MovieParser fileParser() {
        return new KinopoiskFileMovieParser();
    }
}
