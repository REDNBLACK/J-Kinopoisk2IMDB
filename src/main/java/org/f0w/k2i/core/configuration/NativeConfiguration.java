package org.f0w.k2i.core.configuration;

import org.f0w.k2i.core.Main;

import javax.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

@Singleton
public class NativeConfiguration implements Configuration {
    private Preferences preferences;

    public NativeConfiguration(File file) {
        try {
            Preferences.importPreferences(new FileInputStream(file));
        } catch (IOException|InvalidPreferencesFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public NativeConfiguration(String fileName) {
        this(new File(fileName));
    }

    public NativeConfiguration() {
        this(Main.class);
    }

    public NativeConfiguration(Class className) {
        preferences = Preferences.userNodeForPackage(className);
    }

    @Override
    public String get(String key) {
        return get(key, null);
    }

    @Override
    public String get(String key, String defaultValue) {
        return preferences.get(key, defaultValue);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return get(key, defaultValue);
    }

    @Override
    public String getString(String key) {
        return get(key);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    @Override
    public int getInt(String key) {
        return getInt(key, 0);
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return preferences.getFloat(key, defaultValue);
    }

    @Override
    public float getFloat(String key) {
        return getFloat(key, 0.0F);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    @Override
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    @Override
    public <T extends Enum<T>> T getEnum(Class<T> enumeration, String key, String defaultValue) {
        return Enum.valueOf(enumeration, getString(key, defaultValue).toUpperCase());
    }

    @Override
    public <T extends Enum<T>> T getEnum(Class<T> enumeration, String key) {
        return getEnum(enumeration, key, null);
    }
}
