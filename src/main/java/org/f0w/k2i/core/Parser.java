package org.f0w.k2i.core;

import com.google.common.collect.*;
import org.f0w.k2i.core.Components.Configuration;
import org.f0w.k2i.core.EqualityComparators.*;
import org.f0w.k2i.core.Requests.MovieFinders.MovieFinderType;
import org.f0w.k2i.core.Models.Movie;

import java.util.*;

public class Parser {
    private Configuration config;

    public Parser(Configuration config) {
        this.config = config;
    }

    public String parseMovieIMDBId(String data, Movie kinopoiskMovie) {
        String imdbMovieId = null;
        MovieFinderType queryFormat = config.getEnum(MovieFinderType.class, "query_format");
        List<Movie> imdbMovies = parseMovieSearchResult(data, queryFormat);
        EqualityComparatorType comparatorType = config.getEnum(EqualityComparatorType.class, "comparator");
        EqualityComparator<Movie> comparator = EqualityComparatorsFactory.make(comparatorType);

        for (Movie imdbMovie : imdbMovies) {
            if (comparator.areEqual(imdbMovie, kinopoiskMovie)
                && isFirstMovieYearInRangeOfSecondMovieYear(imdbMovie, kinopoiskMovie, config.getInt("year_deviation"))
            ) {
                imdbMovieId = imdbMovie.getImdbId();
                break;
            }
        }

        return imdbMovieId;
    }

    private boolean isFirstMovieYearInRangeOfSecondMovieYear(Movie movie1, Movie movie2, int step) {
        Set<Integer> yearsRange = ContiguousSet.create(
                Range.closed(movie2.getYear() - step, movie2.getYear() + step),
                DiscreteDomain.integers()
        );

        for (Integer year : yearsRange) {
            if (movie1.getYear().equals(year)) {
                return true;
            }
        }

        return false;
    }
}
