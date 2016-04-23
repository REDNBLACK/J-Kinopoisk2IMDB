package org.f0w.k2i.core.comparator.title;

import com.google.common.collect.ImmutableList;
import org.f0w.k2i.core.comparator.AbstractMovieComparator;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.util.string.NumericToWord;
import org.f0w.k2i.core.util.string.Translit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * Uses multiple unique algorithms for movie title comparison.
 */
public final class SmartTitleComparator extends AbstractMovieComparator {
    private static final List<StringModifier> modifiers;

    static {
        List<StringModifier> list = new ArrayList<>();

        // Original string
        list.add(s -> s);

        // Original string without commas
        list.add(s -> replaceChars(s, ",", ""));

        // Original string without colon
        list.add(s -> replaceChars(s, ":", ""));

        // Original string without apostrophes and quotes
        list.add(s -> {
            String[] toReplace = {"'", "«", "»"};
            String[] replacements = Collections.nCopies(toReplace.length, "")
                    .toArray(new String[toReplace.length]);

            return replaceEachRepeatedly(s, toReplace, replacements);
        });

        // Original string with unescaped XML symbols and removed foreign accents
        list.add(s -> stripAccents(unescapeXml(s)));

        // Original string without special symbols like unicode etc
        list.add(s -> s.replaceAll("/\\\\u([0-9a-z]{4})/", ""));

        // Original string with part before dash symbol
        list.add(s -> Arrays.stream(splitByWholeSeparator(s, "-"))
                .findFirst()
                .map(String::trim)
                .orElse(s)
        );

        // Original string with part after dash symbol
        list.add(s -> Arrays.stream(splitByWholeSeparator(s, "-"))
                .reduce((s1, s2) -> s2)
                .map(String::trim)
                .orElse(s)
        );

        // The + Original string
        list.add(s -> "The " + s);

        // Original string with all whitespace characters replaced with plain backspace
        list.add(s -> s.replaceAll("\\s+", " "));

        // Original string with XML symbols replaced with backspace
        list.add(s -> {
            String[] toReplace = {"&#xB;", "&#xC;", "&#x1A;", "&#x1B;"};
            String[] replacements = Collections.nCopies(toReplace.length, " ")
                    .toArray(new String[toReplace.length]);

            return replaceEachRepeatedly(s, toReplace, replacements);
        });

        // Transliterated string
        list.add(Translit::toTranslit);

        // Weakly transliterated string
        list.add(Translit::toWeakerTranslit);

        // Weakly transliterated with lower case and capitalized
        list.add(s -> Translit.toWeakerTranslit(capitalize(s.toLowerCase())));

        // Original string with numeric replaced to text representation
        list.add(s -> Arrays.stream(splitByWholeSeparator(s, null))
                .map(n -> {
                    try {
                        return NumericToWord.convert(Integer.parseInt(n));
                    } catch (NumberFormatException ignored) {
                        return n;
                    }
                })
                .collect(Collectors.joining(" "))
        );

        // Modifiers using symbols mix
        List<String> symbolsMix = Arrays.asList("&", "and", "et");
        symbolsMix.forEach(s1 -> symbolsMix.forEach(s2 -> {
            if (!s1.equals(s2)) {
                list.add(s -> replace(s, s1, s2));
            }
        }));

        modifiers = ImmutableList.copyOf(list);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean areEqual(Movie movie1, Movie movie2) {
        for (StringModifier m1 : modifiers) {
            for (StringModifier m2 : modifiers) {
                String m1Title = m1.modify(movie1.getTitle());
                String m2Title = m2.modify(movie2.getTitle());

                boolean result = m1Title.equals(m2Title);

                LOG.debug(
                        "Comparing title '{}' with title '{}', matches = '{}'",
                        m1Title,
                        m2Title,
                        result
                );

                if (result) {
                    return true;
                }
            }
        }

        return false;
    }

    @FunctionalInterface
    private interface StringModifier {
        String modify(String string);
    }
}