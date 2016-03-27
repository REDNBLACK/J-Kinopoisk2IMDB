package org.f0w.k2i.core.model.repository;

import org.f0w.k2i.core.model.entity.KinopoiskFile;

public interface KinopoiskFileRepository {
    KinopoiskFile save(KinopoiskFile file);

    KinopoiskFile findByHashCode(final String hashCode);
}
