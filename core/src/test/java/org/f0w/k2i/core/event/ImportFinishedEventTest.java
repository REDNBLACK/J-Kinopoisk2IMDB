package org.f0w.k2i.core.event;

import org.f0w.k2i.MovieTestData;
import org.f0w.k2i.core.handler.MovieHandler;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ImportFinishedEventTest {
    @Test
    public void constructorInitialization() {
        List<MovieHandler.Error> errors = Arrays.asList(
                new MovieHandler.Error(MovieTestData.MOVIE_1, "Error 1"),
                new MovieHandler.Error(MovieTestData.MOVIE_2, "Error 2")
        );

        assertEquals(errors, new ImportFinishedEvent(errors).errors);
    }

    @Test
    public void isImmutableError() {
        List<MovieHandler.Error> errors = Collections.singletonList(
                new MovieHandler.Error(new Movie("Inception", 2010, Movie.Type.MOVIE, 9, "tt1375666"), "Error 1")
        );
        ImportFinishedEvent event = new ImportFinishedEvent(errors);

        Movie mutableEntity = errors.get(0).getMovie();
        mutableEntity.setTitle("new title");
        mutableEntity.setYear(9999);
        mutableEntity.setRating(1);

        assertEquals(new Movie("Inception", 2010, Movie.Type.MOVIE, 9, "tt1375666"), event.errors.get(0).getMovie());
    }

    @Test
    public void isImmutableErrorsList() {
        List<MovieHandler.Error> errors = new ArrayList<>(Collections.singletonList(
                new MovieHandler.Error(new Movie("Inception", 2010, Movie.Type.MOVIE, 9, "tt1375666"), "Error 1")
        ));
        ImportFinishedEvent event = new ImportFinishedEvent(errors);

        errors.remove(0);

        assertTrue(errors.isEmpty());
        assertEquals(
                Collections.singletonList(
                        new MovieHandler.Error(new Movie("Inception", 2010, Movie.Type.MOVIE, 9, "tt1375666"), "Error 1")
                ),
                event.errors
        );
    }
}