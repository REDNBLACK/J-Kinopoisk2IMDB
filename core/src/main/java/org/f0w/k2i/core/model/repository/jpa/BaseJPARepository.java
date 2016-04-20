package org.f0w.k2i.core.model.repository.jpa;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.f0w.k2i.core.model.repository.Repository;

import javax.persistence.EntityManager;
import java.io.Serializable;

public abstract class BaseJPARepository<T, ID extends Serializable> implements Repository<T, ID> {
    @Inject
    protected Provider<EntityManager> entityManagerProvider;

    /**
     * {@inheritDoc}
     */
    @Override
    public T find(ID id) {
        return entityManagerProvider.get().find(getType(), id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public T save(T entity) {
        entityManagerProvider.get().persist(entity);

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void delete(T entity) {
        entityManagerProvider.get().remove(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void delete(ID id) {
        EntityManager entityManager = entityManagerProvider.get();

        T entity = entityManager.getReference(getType(), id);

        entityManager.remove(entity);
    }
}
