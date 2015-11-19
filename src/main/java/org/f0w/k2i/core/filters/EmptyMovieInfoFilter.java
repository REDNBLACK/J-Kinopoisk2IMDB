package org.f0w.k2i.core.filters;

import org.f0w.k2i.core.entities.Movie;

import java.util.*;

public class EmptyMovieInfoFilter implements Filter<Movie> {
    @Override
    public List<Movie> filter(List<Movie> list) {
        ArrayList<Movie> movies = new ArrayList<>(list);
        Iterator<Movie> iterator = movies.iterator();

        while (iterator.hasNext()) {
            Movie movie = iterator.next();

            if (movie.getTitle() == null || movie.getYear() == null || movie.getImdbId() == null) {
                iterator.remove();
            }
        }

        return movies;
    }
}
