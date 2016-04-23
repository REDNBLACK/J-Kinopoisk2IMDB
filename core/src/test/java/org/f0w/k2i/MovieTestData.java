package org.f0w.k2i;

import org.f0w.k2i.core.model.entity.Movie;

import java.util.Arrays;
import java.util.List;

public class MovieTestData {
    public static final Movie MOVIE_1 = new Movie("Inception", 2010, 9, "tt1375666");
    public static final Movie MOVIE_2 = new Movie("Breaking Bad", 2008, 10, "tt0903747");
    public static final Movie MOVIE_3 = new Movie("Kokaku kidotai: Stand Alone Complex", 2002, 10, "tt0346314");
    public static final Movie MOVIE_4 = new Movie("Hannibal", 2013, 10, "tt2243973");
    public static final Movie MOVIE_5 = new Movie("Sin City", 2005, 10, "tt0401792");

    public static final List<Movie> MOVIE_LIST = Arrays.asList(MOVIE_5, MOVIE_4, MOVIE_3, MOVIE_2, MOVIE_1);

    public static class TestMovie extends Movie {
        public static Movie copyOf(Movie movie) {
            return new Movie(movie.getTitle(), movie.getYear(), movie.getRating(), movie.getImdbId());
        }
    }
}
