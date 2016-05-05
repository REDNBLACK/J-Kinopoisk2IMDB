package org.f0w.k2i.core.event;

import org.f0w.k2i.core.model.entity.ImportProgress;

/**
 * Indicates that movies import progress has advanced
 */
public final class ImportProgressAdvancedEvent implements Event {
    public final ImportProgress importProgress;
    public final boolean successful;

    public ImportProgressAdvancedEvent(ImportProgress importProgress, boolean successful) {
        this.importProgress = new ImportProgress(importProgress);
        this.successful = successful;
    }
}
