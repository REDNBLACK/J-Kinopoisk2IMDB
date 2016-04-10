package org.f0w.k2i.core.comparator;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.f0w.k2i.core.comparator.title.*;
import org.f0w.k2i.core.comparator.year.DeviationYearComparator;
import org.f0w.k2i.core.comparator.year.EqualsYearComparator;

/**
 * {@link MovieComparator} factory
 */
public class MovieComparatorFactory {
    private final Config config;

    @Inject
    public MovieComparatorFactory(Config config) {
        this.config = config;
    }

    /**
     * Create instance of {@link MovieComparator},
     * using {@link MovieComparator.Type} as argument.
     * @param type Type of MovieComparator
     * @return MovieComparator instance
     */
    public MovieComparator make(MovieComparator.Type type) {
        switch (type) {
            case TITLE_EQUALS:
                return new EqualsTitleComparator();
            case TITLE_CONTAINS:
                return new ContainsTitleComparator();
            case TITLE_STARTS:
                return new StartsWithTitleComparator();
            case TITLE_ENDS:
                return new EndsWithTitleComparator();
            case TITLE_SMART:
                return new SmartTitleComparator();
            case YEAR_DEVIATION:
                return new DeviationYearComparator(config);
            case YEAR_EQUALS:
                return new EqualsYearComparator();
            default:
                throw new UnsupportedOperationException("Comparator of this type not found!");
        }
    }
}
