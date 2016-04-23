package org.f0w.k2i.core.event;

import org.f0w.k2i.ImportProgressTestData;
import org.f0w.k2i.core.handler.MovieHandler;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ImportFinishedEventTest {
    @Test
    public void testConstructorInitialization() {
        List<MovieHandler.Error> expected = Arrays.asList(
                new MovieHandler.Error(
                        ImportProgressTestData.IMPORT_PROGRESS_1,
                        "Error 1"
                ),
                new MovieHandler.Error(
                        ImportProgressTestData.IMPORT_PROGRESS_2,
                        "Error 2"
                )
        );

        ImportFinishedEvent event = new ImportFinishedEvent(expected);

        assertEquals(expected, event.errors);
    }
}