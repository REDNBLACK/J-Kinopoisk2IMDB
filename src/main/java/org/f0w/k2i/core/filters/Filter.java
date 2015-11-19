package org.f0w.k2i.core.filters;

import java.util.List;

public interface Filter<T> {
    List<T> filter(List<T> list);
}
