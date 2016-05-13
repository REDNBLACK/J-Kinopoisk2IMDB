package org.f0w.k2i.core.model.entity;

import com.google.common.collect.ImmutableMap;
import lombok.val;
import org.f0w.k2i.ImportProgressTestData;
import org.junit.Test;

import static org.f0w.k2i.KinopoiskFileTestData.KINOPOISK_FILE_1;
import static org.f0w.k2i.KinopoiskFileTestData.KINOPOISK_FILE_2;
import static org.f0w.k2i.MovieTestData.MOVIE_1;
import static org.f0w.k2i.MovieTestData.MOVIE_2;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.CombinableMatcher.both;
import static org.junit.Assert.*;

public class ImportProgressTest {
    @Test
    public void testEqualsAndHashCode() throws Exception {
        val expected = new ImportProgress(KINOPOISK_FILE_1, MOVIE_1, true, true);
        val actual = new ImportProgress(KINOPOISK_FILE_1, MOVIE_1, true, true);

        assertEquals(expected, actual);
        assertEquals(expected.hashCode(), actual.hashCode());

        val notEqual = new ImmutableMap.Builder<ImportProgress, ImportProgress>()
                .put(new ImportProgress(KINOPOISK_FILE_2, MOVIE_1, true, true),
                        new ImportProgress(KINOPOISK_FILE_1, MOVIE_1, true, true)
                )
                .put(new ImportProgress(KINOPOISK_FILE_1, MOVIE_2, true, true),
                        new ImportProgress(KINOPOISK_FILE_1, MOVIE_1, true, true)
                )
                .put(new ImportProgress(KINOPOISK_FILE_1, MOVIE_1, false, true),
                        new ImportProgress(KINOPOISK_FILE_1, MOVIE_1, true, true)
                )
                .put(new ImportProgress(KINOPOISK_FILE_1, MOVIE_1, true, false),
                        new ImportProgress(KINOPOISK_FILE_1, MOVIE_1, true, true)
                )
                .build();

        notEqual.forEach((ip1, ip2) -> {
            assertNotEquals(ip1, ip2);
            assertNotEquals(ip1.hashCode(), ip2.hashCode());
        });
    }

    @Test
    public void testToString() throws Exception {
        ImportProgressTestData.IMPORT_PROGRESS_LIST.forEach(ip -> assertThat(ip.toString(),
                both(containsString("kinopoiskFile=" + ip.getKinopoiskFile()))
                .and(containsString("movie=" + ip.getMovie()))
                .and(containsString("imported=" + ip.isImported()))
                .and(containsString("rated=" + ip.isRated()))
        ));
    }
}