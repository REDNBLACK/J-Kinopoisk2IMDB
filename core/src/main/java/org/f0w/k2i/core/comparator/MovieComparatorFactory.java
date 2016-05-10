package org.f0w.k2i.core.comparator;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.f0w.k2i.core.comparator.title.*;
import org.f0w.k2i.core.comparator.type.AnyTypeComparator;
import org.f0w.k2i.core.comparator.type.EqualsTypeComparator;
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
     *
     * @param type Type of MovieComparator
     * @return MovieComparator instance
     * @throws IllegalArgumentException If such type not instantiable
     */
    public MovieComparator make(MovieComparator.Type type) {
        switch (type) {
            case YEAR_EQUALS:
                return new EqualsYearComparator();
            case YEAR_DEVIATION:
                return new DeviationYearComparator(config);
            case TYPE_EQUALS:
                return new EqualsTypeComparator();
            case TYPE_ANY:
                return new AnyTypeComparator();
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
            default:
                throw new IllegalArgumentException("Comparator of this type not found!");
        }
    }
}
