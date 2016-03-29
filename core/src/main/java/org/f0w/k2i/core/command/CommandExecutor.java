package org.f0w.k2i.core.command;

import org.f0w.k2i.core.model.entity.ImportProgress;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommandExecutor {
    private final List<MovieCommand> commands;

    public CommandExecutor(List<MovieCommand> commands) {
        this.commands = commands;
    }

    public List<MovieError> execute(ImportProgress importProgress) {
        return commands.stream()
                .map(c -> c.execute(importProgress))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
