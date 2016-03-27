package org.f0w.k2i.core.event;

public class ImportListInitializedEvent implements Event {
    public final int listSize;

    public ImportListInitializedEvent(int listSize) {
        this.listSize = listSize;
    }
}
