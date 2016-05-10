package org.f0w.k2i.core.util.exception;

import org.f0w.k2i.TestHelper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.MalformedURLException;
import java.net.URL;

import static org.f0w.k2i.core.util.exception.ExceptionUtils.rethrowSupplier;
import static org.f0w.k2i.core.util.exception.ExceptionUtils.uncheck;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExceptionUtilsTest {
    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void isConstructorPrivate() throws Exception {
        assertTrue(TestHelper.isConstructorPrivate(ExceptionUtils.class));

        TestHelper.callPrivateConstructor(ExceptionUtils.class);
    }

    @Test
    public void uncheckRethrowsException() throws Exception {
        expected.expect(MalformedURLException.class);
        expected.expectMessage("no protocol: not_existing_url");

        uncheck(() -> new URL("not_existing_url"));
    }

    @Test
    public void uncheckWrapsException() throws Exception {
        assertEquals(new URL("http://google.com"), uncheck(() -> new URL("http://google.com")));
    }

    @Test
    public void rethrowSupplierRethrowsException() throws Exception {
        expected.expect(MalformedURLException.class);
        expected.expectMessage("no protocol: not existing url");

        rethrowSupplier(() -> new URL("not existing url")).get();
    }
}