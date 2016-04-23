package org.f0w.k2i.core.model.repository;

import java.io.Serializable;
import java.util.List;

public interface Repository<T, ID extends Serializable> {
    /**
     * Find entity by id
     *
     * @param id Id of entity
     * @return Found entity
     */
    T find(ID id);

    /**
     * Returns all instances of the type.
     *
     * @return All entities
     */
    List<T> findAll();

    /**
     * Returns all instances of the type with the given IDs.
     *
     * @return All entities having given IDs
     */
    List<T> findAll(Iterable<ID> ids);

    /**
     * Save or update entity
     *
     * @param entity Entity to perform actions
     * @return Entity with updated ID if new, or the same entity
     */
    T save(T entity);

    /**
     * Delete entity
     *
     * @param entity Entity to delete
     */
    void delete(T entity);

    /**
     * Delete entity by ID
     *
     * @param id Of entity
     */
    void delete(ID id);

    /**
     * Get type of Entity
     *
     * @return Entity class
     */
    Class<T> getType();
}
