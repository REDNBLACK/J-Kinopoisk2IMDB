package org.f0w.k2i.core.event;

import org.f0w.k2i.MovieTestData;
import org.f0w.k2i.core.handler.MovieHandler;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ImportFinishedEventTest {
    @Test
    public void testConstructorInitialization() {
        List<MovieHandler.Error> expected = Arrays.asList(
                new MovieHandler.Error(MovieTestData.MOVIE_1, "Error 1"),
                new MovieHandler.Error(MovieTestData.MOVIE_2, "Error 2")
        );

        ImportFinishedEvent event = new ImportFinishedEvent(expected);

        assertEquals(expected, event.errors);
    }
}