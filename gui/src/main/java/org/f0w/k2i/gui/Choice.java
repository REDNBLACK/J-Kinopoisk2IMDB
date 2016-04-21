package org.f0w.k2i.gui;

import java.util.Objects;

class Choice<K, V> {
    private final K value;
    private final V label;

    public Choice(K value) {
        this.value = value;
        this.label = null;
    }

    public Choice(K value, V label) {
        this.value = value;
        this.label = label;
    }

    public K getValue() {
        return value;
    }

    public V getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return String.valueOf(label);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Choice<?, ?> choice = (Choice<?, ?>) o;
        return Objects.equals(value, choice.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
