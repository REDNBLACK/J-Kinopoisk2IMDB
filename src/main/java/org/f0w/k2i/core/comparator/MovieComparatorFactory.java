package org.f0w.k2i.core.comparator;

import com.google.inject.Inject;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.utils.InjectorUtils;

import java.util.List;
import java.util.stream.Collectors;

public class MovieComparatorFactory {
    private final InjectorUtils injectorUtils;

    @Inject
    public MovieComparatorFactory(InjectorUtils injectorUtils) {
        this.injectorUtils = injectorUtils;
    }

    public MovieComparator make(List<String> comparatorsClassesNames) {
        List<MovieComparator> comparators = comparatorsClassesNames.stream()
                .map(c -> InjectorUtils.getClassFromString(c, MovieComparator.class))
                .map(injectorUtils::getInstance)
                .collect(Collectors.toList());

        return new MovieComparatorContainer(comparators);
    }

    private final class MovieComparatorContainer implements MovieComparator {
        protected final List<MovieComparator> comparators;

        public MovieComparatorContainer(List<MovieComparator> comparators) {
            this.comparators = comparators;
        }

        @Override
        public boolean areEqual(Movie movie1, Movie movie2) {
            return comparators.stream()
                    .map(c -> c.areEqual(movie1, movie2))
                    .noneMatch(c -> false);
        }
    }
}
