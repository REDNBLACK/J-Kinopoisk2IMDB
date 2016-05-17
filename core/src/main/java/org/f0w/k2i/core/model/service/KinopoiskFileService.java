package org.f0w.k2i.core.model.service;

import org.f0w.k2i.core.model.entity.KinopoiskFile;

import java.nio.file.Path;

public interface KinopoiskFileService {
    KinopoiskFile find(Path filePath);

    KinopoiskFile save(Path filePath);

    void delete(Path filePath);
}
