package org.f0w.k2i.core.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class CollectionUtils {
    private CollectionUtils() {
        throw new UnsupportedOperationException();
    }

    public static <K,V> Map<K, V> combineLists(List<? extends K> keys, List<? extends V> values) {
        if (keys.size() != values.size()) {
            throw new IllegalArgumentException ("Cannot combine lists with dissimilar sizes");
        }

        Map<K, V> map = new LinkedHashMap<>(values.size());

        for (int i = 0; i < keys.size(); i++) {
            map.put(keys.get(i), values.get(i));
        }

        return map;
    }
}
