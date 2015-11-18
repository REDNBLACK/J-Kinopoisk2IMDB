package org.f0w.k2i.core.configuration;

public interface Configuration {
    String get(String key);

    String get(String key, String defaultValue);

    String getString(String key, String defaultValue);

    String getString(String key);

    int getInt(String key, int defaultValue);

    int getInt(String key);

    float getFloat(String key, float defaultValue);

    float getFloat(String key);

    boolean getBoolean(String key, boolean defaultValue);

    boolean getBoolean(String key);

    <T extends Enum<T>> T getEnum(Class <T> enumeration, String key, String defaultValue);

    <T extends Enum<T>> T getEnum(Class<T> enumeration, String key);
}
