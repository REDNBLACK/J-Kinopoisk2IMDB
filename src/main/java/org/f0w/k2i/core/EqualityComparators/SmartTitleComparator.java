package org.f0w.k2i.core.EqualityComparators;

import com.google.common.collect.ImmutableList;
import com.google.common.html.HtmlEscapers;
import org.f0w.k2i.core.Models.Movie;

import java.util.*;

public class SmartTitleComparator implements EqualityComparator<Movie> {
    private static final List<StringModifier> modifiers;

    interface StringModifier {
        String modify(String string);
    }

    static {
        List<StringModifier> list = new ArrayList<>();

        // Original string
        list.add(new StringModifier() {
            @Override
            public String modify(String string) {
                return string;
            }
        });

        // Original string without commas
        list.add(new StringModifier() {
            @Override
            public String modify(String string) {
                return string.replaceAll(",", "");
            }
        });

        // Original string without colon
        list.add(new StringModifier() {
            @Override
            public String modify(String string) {
                return string.replaceAll(":", "");
            }
        });

        // Original string without apostrophes
        list.add(new StringModifier() {
            @Override
            public String modify(String string) {
                return string.replaceAll("/([\'\\\\x{0027}]|&#39;|&#x27;)/u", "");
            }
        });

        // Original string without special symbols like unicode etc
        list.add(new StringModifier() {
            @Override
            public String modify(String string) {
                return string.replaceAll("/\\\\u([0-9a-z]{4})/", "");
            }
        });

        // Original string with part before dash symbol
        list.add(new StringModifier() {
            @Override
            public String modify(String string) {
                String[] parts = string.split("-");

                return parts[0].trim();
            }
        });

        // Original string with part after dash symbol
        list.add(new StringModifier() {
            @Override
            public String modify(String string) {
                String[] parts = string.split("-");

                return parts.length > 0 ? parts[parts.length - 1].trim() : "";
            }
        });

        // The + Original string
        list.add(new StringModifier() {
            @Override
            public String modify(String string) {
                return "The " + string;
            }
        });

        // Original string with all whitespace characters replaced with plain backspace
        list.add(new StringModifier() {
            @Override
            public String modify(String string) {
                return string.replaceAll("/\\s+/", " ");
            }
        });

        // Original string with replaced foreign characters
        list.add(new StringModifier() {
            @Override
            public String modify(String string) {
                return HtmlEscapers.htmlEscaper()
                        .escape(string)
                        .replaceAll("~&([a-z]{1,2})(acute|cedil|circ|grave|lig|orn|ring|slash|th|tilde|uml);~i", "$1")
                ;
            }
        });

        // Original string with XML symbols replaced with backspace
        list.add(new StringModifier() {
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

                list.add(new StringModifier() {
                    @Override
                    public String modify(String string) {
                        return string.replaceAll(firstSymbol, secondSymbol);
                    }
                });
            }
        }

        modifiers = ImmutableList.copyOf(list);
    }

    @Override
    public boolean areEqual(Movie obj1, Movie obj2) {
        boolean result = false;

        for (StringModifier firstModifier : modifiers) {
            for (StringModifier secondModifier : modifiers) {
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
}