package org.f0w.k2i.core.filters;

import com.google.common.collect.Range;
import org.f0w.k2i.core.entities.Movie;

import java.util.*;

public class MovieYearDeviationFilter implements Filter<Movie> {
    private Movie movie;
    private int deviation;

    public MovieYearDeviationFilter(Movie movie, int deviation) {
        this.movie = movie;
        this.deviation = deviation;
    }

    @Override
    public List<Movie> filter(List<Movie> list) {
        ArrayList<Movie> movies = new ArrayList<>(list);
        Iterator<Movie> iterator = movies.iterator();

        while (iterator.hasNext()) {
            if (!isFirstNumberInRangeOfSecondNumber(iterator.next().getYear(), movie.getYear(), deviation)) {
                iterator.remove();
            }
        }

        return movies;
    }

    private boolean isFirstNumberInRangeOfSecondNumber(int first, int second, int deviation) {
        return Range.closed(second - deviation, second + deviation).contains(first);
    }
}
