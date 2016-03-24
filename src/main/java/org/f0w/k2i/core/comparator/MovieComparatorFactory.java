package org.f0w.k2i.core.comparator;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.f0w.k2i.core.comparator.title.*;
import org.f0w.k2i.core.comparator.year.DeviationYearComparator;

import java.util.Arrays;
import java.util.List;

public class MovieComparatorFactory {
    private final Config config;

    @Inject
    public MovieComparatorFactory(Config config) {
        this.config = config;
    }

    public MovieComparator make(MovieComparator.Type titleComparatorType) {
        switch (titleComparatorType) {
            case SMART:
                return makeSmartComparator();
            case EQUALS:
                return makeEqualsComparator();
            case CONTAINS:
                return makeContainsComparator();
            case STARTS_WITH:
                return makeStartsWithComparator();
            case ENDS_WITH:
                return makeEndsWithComparator();
            default:
                throw new IllegalArgumentException("Unexpected comparator type!");
        }
    }

    private MovieComparator makeSmartComparator() {
        List<MovieComparator> comparators = Arrays.asList(
                new SmartTitleComparator(),
                makeYearDeviationComparator()
        );

        return new MovieComparatorContainer(comparators);
    }

    private MovieComparator makeEqualsComparator() {
        List<MovieComparator> comparators = Arrays.asList(
                new EqualsTitleComparator(),
                makeYearDeviationComparator()
        );

        return new MovieComparatorContainer(comparators);
    }

    private MovieComparator makeContainsComparator() {
        List<MovieComparator> comparators = Arrays.asList(
                new ContainsTitleComparator(),
                makeYearDeviationComparator()
        );

        return new MovieComparatorContainer(comparators);
    }

    private MovieComparator makeStartsWithComparator() {
        List<MovieComparator> comparators = Arrays.asList(
                new StartsWithTitleComparator(),
                makeYearDeviationComparator()
        );

        return new MovieComparatorContainer(comparators);
    }

    private MovieComparator makeEndsWithComparator() {
        List<MovieComparator> comparators = Arrays.asList(
                new EndsWithTitleComparator(),
                makeYearDeviationComparator()
        );

        return new MovieComparatorContainer(comparators);
    }

    private MovieComparator makeYearDeviationComparator() {
        return new DeviationYearComparator(config.getInt("year_deviation"));
    }
}
