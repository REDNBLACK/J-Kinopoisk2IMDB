package org.f0w.k2i.core.util;

public class ReflectionUtils {
    private ReflectionUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T> Class<T> stringToClass(String className, Class<? extends T> targetType) {
        try {
            Class<?> targetClass = Class.forName(className);

            if (isOfTargetType(targetClass, targetType)) {
                return targetType.getClass().cast(targetClass);
            } else {
                throw new IllegalArgumentException("Class is not of targetType");
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static boolean isOfTargetType(String className, Class<?> targetType) {
        try {
            return isOfTargetType(Class.forName(className), targetType);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static boolean isOfTargetType(Class clazz, Class<?> targetType) {
        return targetType.isAssignableFrom(clazz);
    }
}
