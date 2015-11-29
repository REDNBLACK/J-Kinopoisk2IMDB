package org.f0w.k2i.core.exchange.MovieFinders;

import com.google.inject.Inject;
import org.f0w.k2i.core.configuration.Configuration;

public class MovieFindersFactory {
    @Inject
    private Configuration configuration;

    public MovieFinder make(MovieFinderType movieFinderType) {
        MovieFinder movieFinder;

        switch (movieFinderType) {
            case XML:
                movieFinder = new XMLMovieFinder(configuration);
                break;
            case JSON:
                movieFinder = new JSONMovieFinder(configuration);
                break;
            case HTML:
                movieFinder = new HTMLMovieFinder(configuration);
                break;
            case MIXED:
                MovieFinder[] movieFinders = {
                        make(MovieFinderType.XML),
                        make(MovieFinderType.JSON),
                        make(MovieFinderType.HTML)
                };
                movieFinder = new MixedMovieFinder(movieFinders);
                break;
            default:
                throw new IllegalArgumentException("Unexpected MovieFinder type!");
        }

        return movieFinder;
    }
}
