package org.f0w.k2i.console.Comparators;

import org.f0w.k2i.console.Movie;
import java.util.Comparator;

public class ComparatorsFactory {
    public static Comparator<Movie> make(ComparatorType comparatorType) {
        Comparator<Movie> comparator;

        switch (comparatorType) {
            case SMART_COMPARATOR:
                comparator = new SmartComparator();
                break;
            case EQUALS_COMPARATOR:
                comparator = new EqualsComparator();
                break;
            case CONTAINS_COMPARATOR:
                comparator = new ContainsComparator();
                break;
            case STARTS_WITH_COMPARATOR:
                comparator = new StartsWithComparator();
                break;
            case ENDS_WITH_COMPARATOR:
                comparator = new EndsWithComparator();
                break;
            default:
                throw new IllegalArgumentException("Unexpected comparator type!");
        }

        return comparator;
    }
}
