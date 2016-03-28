package org.f0w.k2i.core.event;

public class ImportStartedEvent implements Event {
    public final int listSize;

    public ImportStartedEvent(int listSize) {
        this.listSize = listSize;
    }
}
