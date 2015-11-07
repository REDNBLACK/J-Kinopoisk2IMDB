package org.f0w.k2i.console.EqualityComparators;

import org.f0w.k2i.console.Models.Movie;

public class EqualityComparatorsFactory {
    public static EqualityComparator<Movie> make(EqualityComparatorType equalityComparatorType) {
        EqualityComparator<Movie> comparator;

        switch (equalityComparatorType) {
            case SMART_TITLE_COMPARATOR:
                comparator = new SmartTitleComparator();
                break;
            case EQUALS_TITLE_COMPARATOR:
                comparator = new EqualsTitleComparator();
                break;
            case CONTAINS_TITLE_COMPARATOR:
                comparator = new ContainsTitleComparator();
                break;
            case STARTS_WITH_TITLE_COMPARATOR:
                comparator = new StartsWithTitleComparator();
                break;
            case ENDS_WITH_TITLE_COMPARATOR:
                comparator = new EndsWithTitleComparator();
                break;
            default:
                throw new IllegalArgumentException("Unexpected comparator type!");
        }

        return comparator;
    }
}
