package org.f0w.k2i.console.EqualityComparators;

import org.apache.commons.lang3.StringEscapeUtils;
import org.f0w.k2i.console.Models.Movie;

import java.util.*;

public class SmartTitleComparator implements EqualityComparator<Movie> {
    @Override
    public boolean areEqual(Movie obj1, Movie obj2) {
        boolean result = false;

        for (StringModifier firstModifier : getStringModifiers()) {
            for (StringModifier secondModifier : getStringModifiers()) {
                String firstModifiedString = firstModifier.modify(obj1.getTitle());
                String secondModifiedString = secondModifier.modify(obj2.getTitle());

                if (firstModifiedString.equals(secondModifiedString)) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    interface StringModifier {
        String modify(String string);
    }

    protected List<StringModifier> getStringModifiers() {
        List<StringModifier> modifiers = new ArrayList<>();

        // Original string
        modifiers.add(new StringModifier() {
            @Override
            public String modify(String string) {
                return string;
            }
        });

        // Original string without commas
        modifiers.add(new StringModifier() {
            @Override
            public String modify(String string) {
                return string.replaceAll(",", "");
            }
        });

        // Original string without colon
        modifiers.add(new StringModifier() {
            @Override
            public String modify(String string) {
                return string.replaceAll(":", "");
            }
        });

        // Original string without apostrophes
        modifiers.add(new StringModifier() {
            @Override
            public String modify(String string) {
                return string.replaceAll("/([\'\\\\x{0027}]|&#39;|&#x27;)/u", "");
            }
        });

        // Original string without special symbols like unicode etc
        modifiers.add(new StringModifier() {
            @Override
            public String modify(String string) {
                return string.replaceAll("/\\\\u([0-9a-z]{4})/", "");
            }
        });

        // Original string with part before dash symbol
        modifiers.add(new StringModifier() {
            @Override
            public String modify(String string) {
                String[] parts = string.split("-");

                return parts[0].trim();
            }
        });

        // Original string with part after dash symbol
        modifiers.add(new StringModifier() {
            @Override
            public String modify(String string) {
                String[] parts = string.split("-");

                return parts.length > 0 ? parts[parts.length - 1].trim() : "";
            }
        });

        // The + Original string
        modifiers.add(new StringModifier() {
            @Override
            public String modify(String string) {
                return "The " + string;
            }
        });

        // Original string with all whitespace characters replaced with plain backspace
        modifiers.add(new StringModifier() {
            @Override
            public String modify(String string) {
                return string.replaceAll("/\\s+/", " ");
            }
        });

        // Original string with replaced foreign characters
        modifiers.add(new StringModifier() {
            @Override
            public String modify(String string) {
                return StringEscapeUtils
                    .escapeHtml4(string)
                    .replaceAll("~&([a-z]{1,2})(acute|cedil|circ|grave|lig|orn|ring|slash|th|tilde|uml);~i", "$1")
                ;
            }
        });

        // Original string with XML symbols replaced with backspace
        modifiers.add(new StringModifier() {
            @Override
            public String modify(String string) {
                String[] symbols = {"&#xB;", "&#xC;", "&#x1A;", "&#x1B;"};

                for (String symbol : symbols) {
                    string = string.replaceAll(symbol, " ");
                }

                return string;
            }
        });

        // Modifiers using symbols mix
        String[] symbolsMix = {"&", "and", "et"};
        for (final String firstSymbol : symbolsMix) {
            for (final String secondSymbol : symbolsMix) {
                if (firstSymbol.equals(secondSymbol)) {
                    continue;
                }

                modifiers.add(new StringModifier() {
                    @Override
                    public String modify(String string) {
                        return string.replaceAll(firstSymbol, secondSymbol);
                    }
                });
            }
        }

        return modifiers;
    }
}