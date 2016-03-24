package org.f0w.k2i.core.command;

import org.f0w.k2i.core.model.entity.ImportProgress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractMovieCommand implements Command<ImportProgress> {
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractMovieCommand.class);
}
