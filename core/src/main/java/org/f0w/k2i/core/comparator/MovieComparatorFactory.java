package org.f0w.k2i.core.comparator;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import lombok.val;

import java.util.Arrays;
import java.util.stream.Collectors;

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

    /**
     * Create instance of {@link MovieComparator} of multiple movie comparators,
     * using array of {@link MovieComparator.Type} as argument.
     *
     * @param types Types of MovieComparator
     * @return Multiple MovieComparator instance
     * @throws IllegalArgumentException If such type not instantiable
     */
    public MovieComparator make(MovieComparator.Type... types) {
        val distinctTypes = Arrays.asList(types)
                .stream()
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        if (distinctTypes.size() == 1) {
            return make(distinctTypes.get(0));
        }

        val comparators = distinctTypes.stream().map(this::make).collect(Collectors.toList());

        return (m1, m2) -> comparators.stream().allMatch(c -> c.areEqual(m1, m2));
    }
}
