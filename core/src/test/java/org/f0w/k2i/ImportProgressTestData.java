package org.f0w.k2i;

import org.f0w.k2i.core.model.entity.ImportProgress;

import java.util.Arrays;
import java.util.List;

public class ImportProgressTestData {
    public static final ImportProgress IMPORT_PROGRESS_1 = new ImportProgress(KinopoiskFileTestData.KINOPOISK_FILE_1, MovieTestData.MOVIE_1, true, false);
    public static final ImportProgress IMPORT_PROGRESS_2 = new ImportProgress(KinopoiskFileTestData.KINOPOISK_FILE_1, MovieTestData.MOVIE_2, false, false);
    public static final ImportProgress IMPORT_PROGRESS_3 = new ImportProgress(KinopoiskFileTestData.KINOPOISK_FILE_1, MovieTestData.MOVIE_3, false, true);
    public static final ImportProgress IMPORT_PROGRESS_4 = new ImportProgress(KinopoiskFileTestData.KINOPOISK_FILE_1, MovieTestData.MOVIE_4, false, false);
    public static final ImportProgress IMPORT_PROGRESS_5 = new ImportProgress(KinopoiskFileTestData.KINOPOISK_FILE_1, MovieTestData.MOVIE_5, false, false);

    public static final ImportProgress IMPORT_PROGRESS_6 = new ImportProgress(KinopoiskFileTestData.KINOPOISK_FILE_2, MovieTestData.MOVIE_1, false, false);
    public static final ImportProgress IMPORT_PROGRESS_7 = new ImportProgress(KinopoiskFileTestData.KINOPOISK_FILE_2, MovieTestData.MOVIE_2, true, true);
    public static final ImportProgress IMPORT_PROGRESS_8 = new ImportProgress(KinopoiskFileTestData.KINOPOISK_FILE_2, MovieTestData.MOVIE_3, false, false);
    public static final ImportProgress IMPORT_PROGRESS_9 = new ImportProgress(KinopoiskFileTestData.KINOPOISK_FILE_2, MovieTestData.MOVIE_4, false, true);
    public static final ImportProgress IMPORT_PROGRESS_10 = new ImportProgress(KinopoiskFileTestData.KINOPOISK_FILE_2, MovieTestData.MOVIE_5, true, false);

    public static final List<ImportProgress> IMPORT_PROGRESS_LIST = Arrays.asList(
            IMPORT_PROGRESS_10,
            IMPORT_PROGRESS_9,
            IMPORT_PROGRESS_8,
            IMPORT_PROGRESS_7,
            IMPORT_PROGRESS_6,
            IMPORT_PROGRESS_5,
            IMPORT_PROGRESS_4,
            IMPORT_PROGRESS_3,
            IMPORT_PROGRESS_2,
            IMPORT_PROGRESS_1
    );
}
