package org.f0w.k2i.core.comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class implementing {@link MovieComparator} interface, all comparators must extend it.
 */
public abstract class AbstractMovieComparator implements MovieComparator {
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractMovieComparator.class);
}
