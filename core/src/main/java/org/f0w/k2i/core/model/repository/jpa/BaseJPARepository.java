package org.f0w.k2i.core.model.repository.jpa;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.f0w.k2i.core.model.entity.BaseEntity;
import org.f0w.k2i.core.model.repository.Repository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

public abstract class BaseJPARepository<T extends BaseEntity, ID extends Serializable> implements Repository<T, ID> {
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
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() {
        return entityManagerProvider.get()
                .createQuery("SELECT t FROM " + getType().getSimpleName() + " t")
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll(Iterable<ID> ids) {
        return entityManagerProvider.get()
                .createQuery("SELECT t FROM " + getType().getSimpleName() + " t WHERE t.id IN (:ids)")
                .setParameter("ids", ids)
                .getResultList();
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
