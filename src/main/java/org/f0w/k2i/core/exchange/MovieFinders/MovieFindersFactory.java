package org.f0w.k2i.core.exchange.MovieFinders;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import org.f0w.k2i.core.configuration.Configuration;

import java.util.List;

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
                List<MovieFinder> movieFinders = new ImmutableList.Builder<MovieFinder>()
                        .add(make(MovieFinderType.XML))
                        .add(make(MovieFinderType.JSON))
                        .add(make(MovieFinderType.HTML))
                        .build()
                ;
                movieFinder = new MixedMovieFinder(movieFinders);
                break;
            default:
                throw new IllegalArgumentException("Unexpected MovieFinder type!");
        }

        return movieFinder;
    }
}
