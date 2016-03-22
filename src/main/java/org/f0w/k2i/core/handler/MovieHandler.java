package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.model.entity.ImportProgress;

public interface MovieHandler {
    boolean execute();

    void setImportProgress(ImportProgress progress);
}
