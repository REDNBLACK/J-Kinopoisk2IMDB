package org.f0w.k2i.core.event;

import org.f0w.k2i.ImportProgressTestData;
import org.junit.Test;


import static org.junit.Assert.*;

public class ImportProgressAdvancedEventTest {
    @Test
    public void testConstructorInitialization() {
        ImportProgressAdvancedEvent eventSuccess = new ImportProgressAdvancedEvent(
                ImportProgressTestData.IMPORT_PROGRESS_1, true
        );
        ImportProgressAdvancedEvent eventFailure = new ImportProgressAdvancedEvent(
                ImportProgressTestData.IMPORT_PROGRESS_2, false
        );

        assertEquals(ImportProgressTestData.IMPORT_PROGRESS_1, eventSuccess.importProgress);
        assertTrue(eventSuccess.successful);

        assertEquals(ImportProgressTestData.IMPORT_PROGRESS_2, eventFailure.importProgress);
        assertFalse(eventFailure.successful);
    }
}