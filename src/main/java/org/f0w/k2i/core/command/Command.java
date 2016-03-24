package org.f0w.k2i.core.command;

@FunctionalInterface
public interface Command<T> {
    void execute(T value);
}
