package org.f0w.k2i.core.util.string;

import org.f0w.k2i.TestHelper;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.f0w.k2i.core.util.string.Translit.*;

public class TranslitTest {
    @Test
    public void testPrivateConstructor() throws Exception {
        assertTrue(TestHelper.isConstructorPrivate(Translit.class));

        TestHelper.callPrivateConstructor(Translit.class);
    }

    @Test
    public void testToTranslitOnCyrillic() throws Exception {
        String expected = "Ochen' nu oshen' dlinnaya stroka s Raznymi simvolami russkogo alfavita. Y";
        String actual = toTranslit("Очень ну ощень длинная строка с Разными символами русского алфавита. Ы");

        assertEquals(expected, actual);
    }

    @Test
    public void testToTranslitOnLatin() throws Exception {
        assertEquals("this one must be left as is", toTranslit("this one must be left as is"));
    }

    @Test
    public void testToWeakerTranslitOnCyrrilic() throws Exception {
        String expected = "Ochen nu oshen dlinnaya stroka s Raznymi simvolami russkogo alfavita. Y";
        String actual = toWeakerTranslit("Очень ну ощень длинная строка с Разными символами русского алфавита. Ы");

        assertEquals(expected, actual);
    }

    @Test
    public void testToWeakerTranslitOnLatin() throws Exception {
        assertEquals("this one must be 'left' as is. YI!!!", toWeakerTranslit("this one must be 'left' as is. YI!!!"));
    }
}