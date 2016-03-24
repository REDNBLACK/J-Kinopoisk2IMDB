package org.f0w.k2i.core.controller;

import org.f0w.k2i.core.controller.command.Command;
import org.f0w.k2i.core.model.entity.ImportProgress;

import java.util.List;

class MovieCommandControllerImpl implements MovieCommandController {
    private final List<Command<ImportProgress>> commands;

    public MovieCommandControllerImpl(List<Command<ImportProgress>> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(ImportProgress importProgress) {
        commands.forEach(c -> c.execute(importProgress));
    }
}
