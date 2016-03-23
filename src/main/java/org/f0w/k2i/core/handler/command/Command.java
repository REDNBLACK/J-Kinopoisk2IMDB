package org.f0w.k2i.core.handler.command;

@FunctionalInterface
public interface Command<T> {
    void execute(T value);
}
