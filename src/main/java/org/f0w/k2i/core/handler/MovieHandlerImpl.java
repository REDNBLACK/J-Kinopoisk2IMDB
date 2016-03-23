package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.handler.command.Command;
import org.f0w.k2i.core.model.entity.ImportProgress;

import java.util.List;

public class MovieHandlerImpl implements MovieHandler {
    private final List<Command<ImportProgress>> commands;

    public MovieHandlerImpl(List<Command<ImportProgress>> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(ImportProgress importProgress) {
        commands.forEach(c -> c.execute(importProgress));
    }
}
