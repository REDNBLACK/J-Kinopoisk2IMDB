package org.f0w.k2i.core.comparator;

import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.util.string.NumericToWord;
import org.f0w.k2i.core.util.string.Translit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.CharMatcher.*;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * Uses multiple unique algorithms for movie title comparison.
 */
final class SmartTitleComparator extends AbstractMovieComparator {
    private static final List<StringModifier> modifiers;

    static {
        modifiers = new ImmutableList.Builder<StringModifier>()
                .add(nonModified())
                .addAll(withRemovedSymbols())
                .add(withoutApostrophesAndQuotes())
                .add(withNormalizedForeignerAccents())
                .add(withNormalizedWhitespace())
                .add(withoutSpecialSymbols())
                .addAll(withPartBeforeOneOfSeparators())
                .addAll(withPartAfterOneOfSeparators())
                .addAll(withOneOfPrefixes())
                .addAll(withOneOfPostfixes())
                .add(withNormalizedXMLSymbols())
                .add(transliterated())
                .add(weaklyTransliterated())
                .add(withNumericsReplacedAsWords())
                .addAll(withModifiedSymbols())
                .build();
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

    /**
     * Original string
     * @return StringModifier
     */
    private static StringModifier nonModified() {
        return s -> s;
    }

    /**
     * Original string without one of symbols
     * @return StringModifier list
     */
    private static List<StringModifier> withRemovedSymbols() {
        val symbolsToRemove = Arrays.asList(",", ":", "-", " ", "3-D", "3D", "4D", "4-D");

        return symbolsToRemove.stream()
                .map(symbol -> (StringModifier) string -> replace(string, symbol, "").trim())
                .collect(Collectors.toList());
    }

    /**
     * Original string without apostrophes and quotes
     * @return StringModifier
     */
    private static StringModifier withoutApostrophesAndQuotes() {
        val toReplace = new String[]{"'", "«", "»"};
        val replacements = Collections.nCopies(toReplace.length, "")
                .toArray(new String[toReplace.length]);

        return s -> replaceEachRepeatedly(s, toReplace, replacements);
    }

    /**
     * Original string with unescaped XML symbols and removed foreign accents
     * @return StringModifier
     */
    private static StringModifier withNormalizedForeignerAccents() {
        return s -> stripAccents(unescapeXml(s));
    }

    /**
     * Original string with all whitespace characters replaced with plain backspace
     * @return StringModifier
     * @see StringUtils#normalizeSpace(String)
     */
    private static StringModifier withNormalizedWhitespace() {
        return StringUtils::normalizeSpace;
    }

    /**
     * Original string without special symbols like unicode etc
     * @return StringModifier
     */
    private static StringModifier withoutSpecialSymbols() {
        return s -> javaLetterOrDigit()
                .or(WHITESPACE)
                .or(isNot('.'))
                .precomputed()
                .retainFrom(s);
    }

    /**
     * Original string with part before one of separating symbols
     * @return StringModifier list
     */
    private static List<StringModifier> withPartBeforeOneOfSeparators() {
        val separators = Arrays.asList("-", ":");

        return separators.stream()
                .map(separator -> (StringModifier) string -> Arrays.stream(splitByWholeSeparator(string, separator))
                        .findFirst()
                        .map(String::trim)
                        .orElse(string)
                )
                .collect(Collectors.toList());
    }

    /**
     * Original string with part after one of separating symbols
     * @return StringModifier list
     */
    private static List<StringModifier> withPartAfterOneOfSeparators() {
        val separators = Arrays.asList("-", ":");

        return separators.stream()
                .map(separator -> (StringModifier) string -> Arrays.stream(splitByWholeSeparator(string, separator))
                        .reduce((s1, s2) -> s2)
                        .map(String::trim)
                        .orElse(string)
                )
                .collect(Collectors.toList());
    }

    /**
     * One of the prefixes + original string
     * @return StringModifier list
     */
    private static List<StringModifier> withOneOfPrefixes() {
        val prefixes = Collections.singletonList("The ");

        return prefixes.stream()
                .map(prefix -> (StringModifier) string -> prefix + string)
                .collect(Collectors.toList());
    }

    /**
     * Original string + one of postfixes
     * @return StringModifier list
     */
    private static List<StringModifier> withOneOfPostfixes() {
        val postfixes = Collections.singletonList("u");

        return postfixes.stream()
                .map(postfix -> (StringModifier) string -> string + postfix)
                .collect(Collectors.toList());
    }

    /**
     * Original string with XML symbols replaced with backspace
     * @return StringModifier
     */
    private static StringModifier withNormalizedXMLSymbols() {
        val toReplace = new String[]{"&#xB;", "&#xC;", "&#x1A;", "&#x1B;"};
        val replacements = Collections.nCopies(toReplace.length, " ")
                .toArray(new String[toReplace.length]);

        return s -> replaceEachRepeatedly(s, toReplace, replacements);
    }

    /**
     * Transliterated string
     * @return StringModifier
     * @see Translit#toTranslit(String)
     */
    private static StringModifier transliterated() {
        return Translit::toTranslit;
    }

    /**
     * Weakly transliterated string
     * @return StringModifier
     * @see Translit#toWeakerTranslit(String)
     */
    private static StringModifier weaklyTransliterated() {
        return Translit::toWeakerTranslit;
    }

    /**
     * Original string with numeric replaced to text representation
     * @return StringModifier
     * @see NumericToWord#convert(Integer)
     */
    private static StringModifier withNumericsReplacedAsWords() {
        return s -> Arrays.stream(splitByWholeSeparator(s, null))
                .map(n -> {
                    try {
                        return NumericToWord.convert(Integer.parseInt(n));
                    } catch (NumberFormatException ignored) {
                        return n;
                    }
                })
                .collect(Collectors.joining(" "));
    }

    /**
     * Modifiers using symbols mix
     * @return StringModifier list
     */
    private static List<StringModifier> withModifiedSymbols() {
        val modifiersList = new ArrayList<StringModifier>();
        val symbolsMix = new String[][]{
                {"&", "and", "et"},
                {"le", "the"},
                {"#", "No.", "No. ", "№"},
                {"contre", "vs.", "vs"},
                {"ae", "e"},
                {"Yi", "I"},
                {"½", "1/2", " 1/2", "1/2 "},
                {"¼", "1/4", " 1/4", "1/4 "}
        };

        for (String[] tuples : symbolsMix) {
            for (String symbol1 : tuples) {
                for (String symbol2 : tuples) {
                    if (!symbol1.equals(symbol2)) {
                        modifiersList.add(s -> replace(s, symbol1, symbol2));
                    }
                }
            }
        }

        return modifiersList;
    }
}