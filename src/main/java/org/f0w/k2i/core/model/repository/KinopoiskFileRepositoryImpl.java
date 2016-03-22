package org.f0w.k2i.core.model.repository;

import com.google.inject.persist.PersistService;
import org.f0w.k2i.core.model.entity.KinopoiskFile;

import com.google.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class KinopoiskFileRepositoryImpl implements KinopoiskFileRepository {
    @Inject
    private EntityManager em;

    @Inject
    public KinopoiskFileRepositoryImpl(PersistService service) {
        service.start();
    }

    @Override
    public KinopoiskFile findOrCreate(KinopoiskFile file) {
        try {
            return findByChecksum(file.getChecksum());
        } catch (NoResultException e) {
            em.getTransaction().begin();
            em.persist(file);
            em.getTransaction().commit();

            return file;
        }
    }

    @Override
    public KinopoiskFile findByChecksum(String checksum) {
        TypedQuery<KinopoiskFile> query = em.createQuery(
                "FROM KinopoiskFile kf WHERE kf.checksum = :checksum",
                KinopoiskFile.class
        );
        query.setParameter("checksum", checksum);

        return query.getSingleResult();
    }
}
