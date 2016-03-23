package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.model.entity.ImportProgress;

import java.util.List;
import java.util.function.Consumer;

@FunctionalInterface
public interface MovieHandler {
    void execute(List<ImportProgress> importProgressList, Consumer<ImportProgress> onSuccess, Consumer<ImportProgress> everyTime);
}
