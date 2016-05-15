package org.f0w.k2i.core.util.parser;

import org.f0w.k2i.core.DocumentSourceType;

public final class MovieParsers {
    private MovieParsers() {
    }

    public static MovieParser ofSourceType(DocumentSourceType documentSourceType) {
        switch (documentSourceType) {
            case XML: return new XMLMovieParser();
            case JSON: return new JSONMovieParser();
            case HTML: return new HTMLMovieParser();
            default: throw new IllegalArgumentException("Unsupported format!");
        }
    }

    public static MovieParser fileParser() {
        return new FileMovieParser();
    }
}
