package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.model.entity.ImportProgress;

import java.util.List;

@FunctionalInterface
public interface MovieHandler {
    void execute(List<ImportProgress> importProgressList);
}
