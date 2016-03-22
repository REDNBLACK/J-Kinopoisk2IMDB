package org.f0w.k2i.core.utils;

public class StringHelper {
    public static <T extends Enum<T>> T toEnum(String key, Class<T> enumeration) {
        return Enum.valueOf(enumeration, key);
    }
}
