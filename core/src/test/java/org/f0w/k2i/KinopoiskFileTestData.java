package org.f0w.k2i;

import org.f0w.k2i.core.model.entity.KinopoiskFile;

import java.util.Arrays;
import java.util.List;

public class KinopoiskFileTestData {
    public static final KinopoiskFile KINOPOISK_FILE_1 = new KinopoiskFile("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
    public static final KinopoiskFile KINOPOISK_FILE_2 = new KinopoiskFile("01e3f9c39c569b9c0eba926d24dbcac9a8d2c7e5ac1e75945948a7af04a5e053");

    public static final List<KinopoiskFile> KINOPOISK_FILE_LIST = Arrays.asList(KINOPOISK_FILE_2, KINOPOISK_FILE_1);
}
