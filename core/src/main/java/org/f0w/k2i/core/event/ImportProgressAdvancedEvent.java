package org.f0w.k2i.core.event;

public final class ImportProgressAdvancedEvent implements Event {
    public final boolean successful;

    public ImportProgressAdvancedEvent(boolean successful) {
        this.successful = successful;
    }
}
