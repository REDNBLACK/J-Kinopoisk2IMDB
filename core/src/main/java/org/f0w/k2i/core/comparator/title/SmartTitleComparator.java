package org.f0w.k2i.core.comparator.title;

import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.f0w.k2i.core.comparator.AbstractMovieComparator;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.util.string.NumericToWord;
import org.f0w.k2i.core.util.string.Translit;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.CharMatcher.*;
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

        // Original string without one of symbols
        val symbolsToRemove = Arrays.asList(",", ":", "-", " ", "3-D", "3D", "4D", "4-D");
        symbolsToRemove.forEach(symbol -> list.add(s -> replace(s, symbol, "").trim()));

        // Original string without apostrophes and quotes
        list.add(s -> {
            String[] toReplace = {"'", "«", "»"};
            String[] replacements = Collections.nCopies(toReplace.length, "")
                    .toArray(new String[toReplace.length]);

            return replaceEachRepeatedly(s, toReplace, replacements);
        });

        // Original string with unescaped XML symbols and removed foreign accents
        list.add(s -> stripAccents(unescapeXml(s)));

        // Original string with all whitespace characters replaced with plain backspace
        list.add(StringUtils::normalizeSpace);

        // Original string without special symbols like unicode etc
        list.add(s -> javaLetterOrDigit().or(WHITESPACE).or(isNot('.')).precomputed().retainFrom(s));

        // Original string with part before or after one of separating symbols
        val separatingSymbols = Arrays.asList("-", ":");
        separatingSymbols.forEach(separator -> {
            list.add(s -> Arrays.stream(splitByWholeSeparator(s, separator))
                    .findFirst()
                    .map(String::trim)
                    .orElse(s)
            );
            list.add(s -> Arrays.stream(splitByWholeSeparator(s, separator))
                    .reduce((s1, s2) -> s2)
                    .map(String::trim)
                    .orElse(s)
            );
        });

        // One of the prefixes + original string
        val prefixes = Collections.singletonList("The ");
        prefixes.forEach(p -> list.add(s -> p + s));

        // Original string + one of postfixes
        val postfixes = Collections.singletonList("u");
        postfixes.forEach(p -> list.add(s -> s + p));

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
        val symbolsMix = Arrays.asList(
                Arrays.asList("&", "and", "et"),
                Arrays.asList("le", "the"),
                Arrays.asList("#", "No.", "No. ", "№"),
                Arrays.asList("contre", "vs.", "vs"),
                Arrays.asList("ae", "e"),
                Arrays.asList("Yi", "I"),
                Arrays.asList("½", "1/2", " 1/2", "1/2 "),
                Arrays.asList("¼", "1/4", " 1/4", "1/4 ")
        );
        symbolsMix.forEach(tuples1 -> {
            tuples1.forEach(symbol1 -> {
                tuples1.forEach(symbol2 -> {
                    if (!symbol1.equals(symbol2)) {
                        list.add(s -> replace(s, symbol1, symbol2));
                    }
                });
            });
        });

        modifiers = ImmutableList.copyOf(list);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean areEqual(@NonNull Movie movie1, @NonNull Movie movie2) {
        val title1 = movie1.getTitle();
        val title2 = movie2.getTitle();

        for (StringModifier m1 : modifiers) {
            for (StringModifier m2 : modifiers) {
                boolean result = m1.modify(title1).equalsIgnoreCase(m2.modify(title2));

                if (result) {
                    LOG.debug("Found match when comparing title '{}' with title '{}'", title1, title2);
                    return true;
                }
            }
        }

        LOG.debug("No matches found when comparing title '{}' with title '{}'!", title1, title2);

        return false;
    }

    @FunctionalInterface
    private interface StringModifier {
        String modify(@NonNull String string);
    }
}