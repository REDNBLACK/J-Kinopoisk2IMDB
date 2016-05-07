package org.f0w.k2i.core.util.string;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

/**
 * Utility class that transliterates russian text.
 * For example:
 * Шерлок Холмс и доктор Ватсон: Знакомство - Sherlok Kholms i doktor Vatson: Znakomstvo
 */
public final class Translit {
    private static final String[] charTable = new String[81];
    private static final String[] weakerCharTable;
    private static final char START_CHAR = 'Ё';

    static {
        charTable['А' - START_CHAR] = "A";
        charTable['Б' - START_CHAR] = "B";
        charTable['В' - START_CHAR] = "V";
        charTable['Г' - START_CHAR] = "G";
        charTable['Д' - START_CHAR] = "D";
        charTable['Е' - START_CHAR] = "E";
        charTable['Ё' - START_CHAR] = "E";
        charTable['Ж' - START_CHAR] = "Zh";
        charTable['З' - START_CHAR] = "Z";
        charTable['И' - START_CHAR] = "I";
        charTable['Й' - START_CHAR] = "I";
        charTable['К' - START_CHAR] = "K";
        charTable['Л' - START_CHAR] = "L";
        charTable['М' - START_CHAR] = "M";
        charTable['Н' - START_CHAR] = "N";
        charTable['О' - START_CHAR] = "O";
        charTable['П' - START_CHAR] = "P";
        charTable['Р' - START_CHAR] = "R";
        charTable['С' - START_CHAR] = "S";
        charTable['Т' - START_CHAR] = "T";
        charTable['У' - START_CHAR] = "U";
        charTable['Ф' - START_CHAR] = "F";
        charTable['Х' - START_CHAR] = "Kh";
        charTable['Ц' - START_CHAR] = "Ts";
        charTable['Ч' - START_CHAR] = "Ch";
        charTable['Ш' - START_CHAR] = "Sh";
        charTable['Щ' - START_CHAR] = "Sh";
        charTable['Ъ' - START_CHAR] = "'";
        charTable['Ы' - START_CHAR] = "Y";
        charTable['Ь' - START_CHAR] = "'";
        charTable['Э' - START_CHAR] = "E";
        charTable['Ю' - START_CHAR] = "U";
        charTable['Я' - START_CHAR] = "Ya";

        fillWithLowerCaseChars(charTable);

        weakerCharTable = Arrays.copyOf(charTable, charTable.length);
        weakerCharTable['Ъ' - START_CHAR] = "";
        weakerCharTable['Ь' - START_CHAR] = "";
        weakerCharTable['Ё' - START_CHAR] = "Yo";

        fillWithLowerCaseChars(weakerCharTable);
    }

    private Translit() {
    }

    /**
     * Adds lowercase copy of already existing uppercase characters into table
     *
     * @param table Table to add chars into
     */
    private static void fillWithLowerCaseChars(String[] table) {
        for (int i = 0; i < table.length; i++) {
            char idx = (char) ((char) i + START_CHAR);
            char lower = new String(new char[]{idx}).toLowerCase().charAt(0);
            if (table[i] != null) {
                table[lower - START_CHAR] = table[i].toLowerCase();
            }
        }
    }

    /**
     * Replaces chars in text using replacements from given table of chars.
     *
     * @param text      Text to replace
     * @param charTable Table of chars
     * @return Replaced text
     */
    private static String replaceChars(String text, String[] charTable) {
        char[] charBuffer = text.toCharArray();

        StringBuilder sb = new StringBuilder(text.length());

        for (char symbol : charBuffer) {
            int i = symbol - START_CHAR;

            if (i >= 0 && i < charTable.length) {
                String replace = charTable[i];
                sb.append(replace == null ? symbol : replace);
            } else {
                sb.append(symbol);
            }
        }

        return sb.toString();
    }

    /**
     * Transliterates russian text.
     * For example:
     * Шерлок Холмс и доктор Ватсон: Знакомство - Sherlok Kholms i doktor Vatson: Znakomstvo
     *
     * @param text Text to replaceChars
     * @return Transliterated text
     */
    public static String toTranslit(String text) {
        return replaceChars(text, charTable);
    }

    /**
     * Works the same as {@link Translit#toTranslit(String)},
     * except using different chars and some chars pairs are replaced.
     *
     * @param text Text to replaceChars
     * @return Transliterated text
     */
    public static String toWeakerTranslit(String text) {
        final StringBuilder builder = new StringBuilder(replaceChars(text, weakerCharTable));

        Map<String, Pair<String, String>> pairsToReplace = new ImmutableMap.Builder<String, Pair<String, String>>()
                .put("ый", Pair.of("yi", "yy"))
                .put("ищ", Pair.of("ish", "ishch"))
                .build();

        pairsToReplace.forEach((search, replacements) -> {
            if (containsIgnoreCase(text, search)) {
                String replaced = builder.toString().replaceAll(
                        "(?i)" + replacements.getKey(),
                        replacements.getValue()
                );

                builder.replace(0, builder.length(), replaced);
            }
        });

        return builder.toString();
    }
}