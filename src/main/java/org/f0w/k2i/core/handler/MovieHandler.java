package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.model.entity.KinopoiskFile;

public interface MovieHandler {
    int execute();

    void setKinopoiskFile(KinopoiskFile kinopoiskFile);
}
