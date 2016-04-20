package org.f0w.k2i.core.model.repository;

import org.f0w.k2i.core.model.entity.KinopoiskFile;

public interface KinopoiskFileRepository extends Repository<KinopoiskFile, Long> {
    /**
     * Find file by hashCode
     *
     * @param hashCode Hashcode of file
     * @return Found file or null
     */
    KinopoiskFile findByHashCode(final String hashCode);

    /**
     * {@inheritDoc}
     */
    @Override
    default Class<KinopoiskFile> getType() {
        return KinopoiskFile.class;
    }
}
