package org.f0w.k2i.core.util.string;

import org.f0w.k2i.TestHelper;
import org.junit.Test;

import static org.f0w.k2i.core.util.string.NumericToWord.convert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NumericToWordTest {
    @Test
    public void testPrivateConstructor() throws Exception {
        assertTrue(TestHelper.isConstructorPrivate(NumericToWord.class));

        TestHelper.callPrivateConstructor(NumericToWord.class);
    }

    @Test
    public void testSimpleConvert() throws Exception {
        assertEquals("Three", convert(3));
    }

    @Test
    public void testComplexConvert() throws Exception {
        assertEquals("Four hundred Fifty One", convert(451));
    }
}