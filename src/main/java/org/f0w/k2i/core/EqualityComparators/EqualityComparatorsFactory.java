package org.f0w.k2i.core.EqualityComparators;

import org.f0w.k2i.core.Models.Movie;

public class EqualityComparatorsFactory {
    public static EqualityComparator<Movie> make(EqualityComparatorType equalityComparatorType) {
        EqualityComparator<Movie> comparator;

        switch (equalityComparatorType) {
            case SMART:
                comparator = new SmartTitleComparator();
                break;
            case EQUALS:
                comparator = new EqualsTitleComparator();
                break;
            case CONTAINS:
                comparator = new ContainsTitleComparator();
                break;
            case STARTS_WITH:
                comparator = new StartsWithTitleComparator();
                break;
            case ENDS_WITH:
                comparator = new EndsWithTitleComparator();
                break;
            default:
                throw new IllegalArgumentException("Unexpected comparator type!");
        }

        return comparator;
    }
}
