package org.f0w.k2i.core.model.repository.jpa;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.repository.KinopoiskFileRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class JpaKinopoiskFileRepositoryImpl implements KinopoiskFileRepository {
    @Inject
    private Provider<EntityManager> emProvider;

    @Override
    @Transactional
    public void delete(KinopoiskFile file) {
        emProvider.get().remove(file);
    }

    @Override
    @Transactional
    public KinopoiskFile save(KinopoiskFile file) {
        emProvider.get().persist(file);

        return file;
    }

    @Override
    public KinopoiskFile findByHashCode(final String hashCode) {
        TypedQuery<KinopoiskFile> query = emProvider.get().createQuery(
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
