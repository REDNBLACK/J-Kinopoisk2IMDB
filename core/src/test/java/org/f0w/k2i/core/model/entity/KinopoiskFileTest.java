package org.f0w.k2i.core.model.entity;

import org.f0w.k2i.KinopoiskFileTestData;
import org.junit.Test;

import static org.junit.Assert.*;

public class KinopoiskFileTest {
    @Test
    public void testEqualsAndHashCode() throws Exception {
        assertEquals(new KinopoiskFile("hashcode1"), new KinopoiskFile("hashcode1"));
        assertEquals(new KinopoiskFile("hashcode1").hashCode(), new KinopoiskFile("hashcode1").hashCode());

        assertNotEquals(new KinopoiskFile("hashcode1"), new KinopoiskFile("hashcode2"));
        assertNotEquals(new KinopoiskFile("hashcode1").hashCode(), new KinopoiskFile("hashcode2").hashCode());
    }

    @Test
    public void testToString() throws Exception {
        KinopoiskFileTestData.KINOPOISK_FILE_LIST.forEach(kf -> assertTrue(
                kf.toString().contains("hashCode=" + kf.getHashCode())
        ));
    }
}