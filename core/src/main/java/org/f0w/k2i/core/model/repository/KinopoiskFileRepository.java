package org.f0w.k2i.core.model.repository;

import org.f0w.k2i.core.model.entity.KinopoiskFile;

public interface KinopoiskFileRepository {
    void delete(KinopoiskFile file);

    KinopoiskFile save(KinopoiskFile file);

    KinopoiskFile findByHashCode(final String hashCode);
}
