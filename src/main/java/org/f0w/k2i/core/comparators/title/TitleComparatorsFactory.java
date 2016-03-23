package org.f0w.k2i.core.comparators.title;

import org.f0w.k2i.core.comparators.EqualityComparator;
import org.f0w.k2i.core.model.entity.Movie;

public class TitleComparatorsFactory {
    private TitleComparatorsFactory() {
        throw new UnsupportedOperationException();
    }

    public static EqualityComparator<Movie> make(TitleComparatorType titleComparatorType) {
        EqualityComparator<Movie> comparator;

        switch (titleComparatorType) {
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
