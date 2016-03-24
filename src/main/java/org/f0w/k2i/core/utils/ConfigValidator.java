package org.f0w.k2i.core.utils;

import com.typesafe.config.Config;
import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.exchange.finder.MovieFinder;
import org.f0w.k2i.core.controller.MovieCommandController;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.*;
import static com.google.common.base.Strings.isNullOrEmpty;

public class ConfigValidator {
    private ConfigValidator() {
        throw new UnsupportedOperationException();
    }

    public static Config checkValid(Config config) {
        checkNotNull(config);

        checkAuth(config.getString("auth"));
        checkMode(config.getString("mode"));
        checkList(config.getString("list"), config.getString("mode"));

        checkQueryFormat(config.getString("query_format"));
        checkComparators(config.getStringList("comparators"));

        checkUserAgent(config.getString("user_agent"));
        checkYearDeviation(config.getInt("year_deviation"));
        checkTimeout(config.getInt("timeout"));

        return config;
    }

    private static void checkUserAgent(final String userAgent) {
        final String message = "UserAgent setting is not valid!";

        checkArgument(!isNullOrEmpty(userAgent), message);
    }

    private static void checkYearDeviation(final Integer yearDeviation) {
        final String message = "YearDeviation setting is not valid!";

        checkArgument(yearDeviation > 0, message);
    }

    private static void checkTimeout(final Integer timeout) {
        final String message = "TimeOut setting is not valid!";

        checkArgument(timeout >= 1000, message);
    }

    private static void checkQueryFormat(final String queryFormat) {
        final String message = "QueryFormat setting is not valid!";

        try {
            MovieFinder.Type.valueOf(queryFormat);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void checkComparators(List<String> comparators) {
        final String message = "Comparators setting is not valid!";

        boolean allOfMovieComparatorType = comparators.stream()
                .allMatch(c -> InjectorUtils.isOfTargetType(c, MovieComparator.class));

        checkArgument(allOfMovieComparatorType, message);
    }

    private static void checkAuth(final String auth) {
        final String message = "Auth setting is not valid!";

        checkArgument(auth.length() > 10, message);
    }

    private static void checkList(final String list, final String mode) {
        final String message = "List setting is not valid!";

        if ("".equals(list)) {
            MovieCommandController.Type movieHandlerType = MovieCommandController.Type.valueOf(mode);

            if (movieHandlerType.equals(MovieCommandController.Type.COMBINED)
                || movieHandlerType.equals(MovieCommandController.Type.ADD_TO_WATCHLIST)
            ) {
                throw new IllegalArgumentException(message + " Set to null, but required for current mode setting");
            }
        } else {
            checkArgument(list.startsWith("ls"), message);
            checkArgument(list.length() >= 3, message);
        }
    }

    private static void checkMode(final String mode) {
        final String message = "Mode setting is not valid!";

        try {
             MovieCommandController.Type.valueOf(mode);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message);
        }
    }
}
