package org.f0w.k2i;

import org.f0w.k2i.core.model.entity.Movie;

import java.util.Arrays;
import java.util.List;

public class MovieTestData {
    public static final Movie MOVIE_1 = new Movie("Inception", 2010, Movie.Type.MOVIE, 9, "tt1375666");
    public static final Movie MOVIE_2 = new Movie("Breaking Bad", 2008, Movie.Type.SERIES, 10, "tt0903747");
    public static final Movie MOVIE_3 = new Movie("Kokaku kidotai: Stand Alone Complex", 2002, Movie.Type.SERIES, 10, "tt0346314");
    public static final Movie MOVIE_4 = new Movie("Hannibal", 2013, Movie.Type.SERIES, 10, "tt2243973");
    public static final Movie MOVIE_5 = new Movie("Sin City", 2005, Movie.Type.MOVIE, 10, "tt0401792");
    public static final Movie MOVIE_6 = new Movie("Шерлок Холмс и доктор Ватсон: Знакомство", 1979, Movie.Type.MOVIE, 10, "tt0079902");
    public static final Movie MOVIE_7 = new Movie("Операция Ы и другие приключения Шурика", 1965, Movie.Type.MOVIE, 10, "tt0059550");

    public static final List<Movie> MOVIES_LIST = Arrays.asList(MOVIE_7, MOVIE_6, MOVIE_5, MOVIE_4, MOVIE_3, MOVIE_2, MOVIE_1);
}
