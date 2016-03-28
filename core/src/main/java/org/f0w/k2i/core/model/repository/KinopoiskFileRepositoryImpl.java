package org.f0w.k2i.core.model.repository;

import com.google.inject.persist.Transactional;
import org.f0w.k2i.core.model.entity.KinopoiskFile;

import com.google.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class KinopoiskFileRepositoryImpl implements KinopoiskFileRepository {
    @Inject
    private EntityManager em;

    @Override
    @Transactional
    public KinopoiskFile save(KinopoiskFile file) {
        em.getTransaction().begin();
        em.persist(file);
        em.getTransaction().commit();

        return file;
    }

    @Override
    public KinopoiskFile findByHashCode(final String hashCode) {
        TypedQuery<KinopoiskFile> query = em.createQuery(
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
