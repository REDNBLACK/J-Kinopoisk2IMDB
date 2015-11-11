package org.f0w.k2i.console;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private final Properties properties = new Properties();

    public Configuration() {}

    public Configuration(File file) {
        loadSettings(file);
    }

    public Configuration(String fileName) {
        this(new File(fileName));
    }

    public void loadSettings(File file) {
        try (FileReader reader = new FileReader(file)) {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSettings(String fileName) {
        loadSettings(new File(fileName));
    }

    public String get(String key, Object defaultValue) {
        return properties.getProperty(key, String.valueOf(defaultValue));
    }

    public String get(String key) {
        return get(key, null);
    }

    public String getString(String key, String defaultValue) {
        return get(key, defaultValue);
    }

    public String getString(String key) {
        return get(key, "");
    }

    public int getInt(String key, int defaultValue) {
        return Integer.parseInt(get(key, defaultValue));
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key, 0));
    }

    public float getFloat(String key, float defaultValue) {
        return Float.parseFloat(get(key, defaultValue));
    }

    public float getFloat(String key) {
        return Float.parseFloat(get(key, 0L));
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, defaultValue));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key, false));
    }

    public <T extends Enum<T>> T getEnum(Class <T> enumeration, String key, String defaultValue) {
        return Enum.valueOf(enumeration, get(key, defaultValue));
    }

    public <T extends Enum<T>> T getEnum(Class<T> enumeration, String key) {
        return Enum.valueOf(enumeration, get(key));
    }
}
