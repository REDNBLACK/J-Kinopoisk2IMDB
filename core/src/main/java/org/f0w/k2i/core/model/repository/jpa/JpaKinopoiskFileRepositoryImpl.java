package org.f0w.k2i.core.model.repository.jpa;

import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.repository.KinopoiskFileRepository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class JpaKinopoiskFileRepositoryImpl extends BaseJPARepository<KinopoiskFile, Long>
        implements KinopoiskFileRepository
{
    /** {@inheritDoc} */
    @Override
    public KinopoiskFile findByHashCode(final String hashCode) {
        TypedQuery<KinopoiskFile> query = entityManagerProvider.get().createQuery(
                "FROM KinopoiskFile kf WHERE kf.hashCode = :hashCode",
                KinopoiskFile.class
        );
        query.setParameter("hashCode", hashCode);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
