package org.f0w.k2i.core.event;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ImportStartedEventTest {
    @Test
    public void testConstructorInitialization() {
        ImportStartedEvent event = new ImportStartedEvent(5);

        assertEquals(5, event.listSize);
    }
}