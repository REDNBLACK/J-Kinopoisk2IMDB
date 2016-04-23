package org.f0w.k2i.core.util.exception;

import org.f0w.k2i.TestHelper;
import org.f0w.k2i.core.util.MovieUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.URL;

import static org.junit.Assert.*;
import static org.f0w.k2i.core.util.exception.ExceptionUtils.*;

public class ExceptionUtilsTest {
    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void testPrivateConstructor() throws Exception {
        assertTrue(TestHelper.isConstructorPrivate(ExceptionUtils.class));

        TestHelper.callPrivateConstructor(ExceptionUtils.class);
    }

    @Test
    public void testUncheckRethrowsException() throws Exception {
        expected.expect(KinopoiskToIMDBException.class);
        expected.expectMessage("no protocol: not_existing_url");

        uncheck(() -> new URL("not_existing_url"));
    }

    @Test
    public void testUncheckedWrapsException() throws Exception {
        URL expected = new URL("http://google.com");
        URL actual = uncheck(() -> new URL("http://google.com"));

        assertEquals(expected, actual);
    }
}