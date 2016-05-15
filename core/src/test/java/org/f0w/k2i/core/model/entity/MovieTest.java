package org.f0w.k2i.core.model.entity;

import com.google.common.collect.ImmutableMap;
import lombok.val;
import org.f0w.k2i.MovieTestData;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.CombinableMatcher.both;
import static org.junit.Assert.*;

public class MovieTest {
    @Test
    public void testEqualsAndHashCode() throws Exception {
        val equalMovies = new ImmutableMap.Builder<Movie, Movie>()
                .put(new Movie("Inception", 2010), new Movie("Inception", 2010))
                .put(new Movie("Sin City", 2005), new Movie("Sin City", 2005, Movie.Type.MOVIE, 10, "tt1"))
                .put(new Movie("Breaking Bad", 2008), new Movie("Breaking Bad", 2008, Movie.Type.MOVIE, 10, "tt1"))
                .put(new Movie("Hannibal", 2013, Movie.Type.MOVIE, 5, "tt1"),
                        new Movie("Hannibal", 2013, Movie.Type.MOVIE, 10, "tt2")
                )
                .build();

        equalMovies.forEach((movie1, movie2) -> {
            assertEquals(movie1, movie2);
            assertEquals(movie1.hashCode(), movie2.hashCode());
        });

        val notEqualMovies = new ImmutableMap.Builder<Movie, Movie>()
                .put(new Movie("Inception", 2010), new Movie("Inception", 2005))
                .put(new Movie("Inception 2", 2010), new Movie("Inception", 2010))
                .build();

        notEqualMovies.forEach((movie1, movie2) -> {
            assertNotEquals(movie1, movie2);
            assertNotEquals(movie1.hashCode(), movie2.hashCode());
        });
    }

    @Test
    public void testToString() throws Exception {
        MovieTestData.MOVIES_LIST.forEach(movie -> assertThat(movie.toString(),
                both(containsString("title=" + movie.getTitle()))
                .and(containsString("year=" + movie.getYear()))
                .and(containsString("rating=" + movie.getRating()))
                .and(containsString("imdbId=" + movie.getImdbId()))
        ));
    }

    @Test
    public void testIsEmptyTitle() throws Exception {
        assertFalse(new Movie("Inception", 2010).isEmptyTitle());
        assertTrue(new Movie("null", 2010).isEmptyTitle());
    }

    @Test
    public void testIsEmptyYear() throws Exception {
        assertFalse(new Movie("Inception", 2010).isEmptyYear());
        assertTrue(new Movie("Inception", 0).isEmptyYear());
    }

    @Test
    public void testIsEmptyIMDBId() throws Exception {
        assertFalse(new Movie("Inception", 2010).setImdbId("tt210240").isEmptyIMDBId());
        assertTrue(new Movie("Inception", 2010, Movie.Type.MOVIE, null, null).isEmptyIMDBId());
    }

    @Test
    public void testIsEmptyRating() throws Exception {
        assertFalse(new Movie("Inception", 2010).setRating(10).isEmptyRating());
        assertTrue(new Movie("Inception", 2010).setRating(null).isEmptyRating());
    }
}