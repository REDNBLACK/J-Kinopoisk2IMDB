package org.f0w.k2i.core.util.string;

/**
 * Utility class that transliterates russian text.
 * For example:
 * Шерлок Холмс и доктор Ватсон: Знакомство - Sherlok Kholms i doktor Vatson: Znakomstvo
 */
public final class Translit {
    private static final String[] charTable = new String[81];

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

        for (int i = 0; i < charTable.length; i++) {
            char idx = (char) ((char) i + START_CHAR);
            char lower = new String(new char[]{idx}).toLowerCase().charAt(0);
            if (charTable[i] != null) {
                charTable[lower - START_CHAR] = charTable[i].toLowerCase();
            }
        }
    }

    private Translit() {
        throw new UnsupportedOperationException();
    }

    public static String toTranslit(String text) {
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

    public static String toWeakerTranslit(String text) {
        return toTranslit(text).replace("'", "")
                .replace("yi", "yy");
    }
}