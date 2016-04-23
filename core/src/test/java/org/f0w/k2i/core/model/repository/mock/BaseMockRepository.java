package org.f0w.k2i.core.model.repository.mock;

import org.f0w.k2i.core.model.entity.BaseEntity;
import org.f0w.k2i.core.model.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class BaseMockRepository<T extends BaseEntity> implements Repository<T, Long> {
    protected Map<Long, T> storage = new ConcurrentHashMap<>();
    protected AtomicLong counter = new AtomicLong(0);

    @Override
    public T find(Long id) {
        return storage.getOrDefault(id, null);
    }

    @Override
    public List<T> findAll(Iterable<Long> ids) {
        List<Long> idList = StreamSupport.stream(ids.spliterator(), false)
                .collect(Collectors.toList());

        return storage.values().stream()
                .filter(v -> idList.contains(v.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public T save(T entity) {
        if (entity.getId() == null) {
            entity.setId(counter.incrementAndGet());
        }

        return storage.put(entity.getId(), entity);
    }

    @Override
    public void delete(T entity) {
        storage.remove(entity.getId());
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}
