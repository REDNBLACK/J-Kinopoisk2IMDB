package org.f0w.k2i;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public final class TestHelper {
    /**
     * Checks that constructor of the given class is private
     *
     * @param clazz Class to check
     * @return Is private constructor
     * @throws Exception
     * @see Modifier#isPrivate(int)
     */
    public static boolean isConstructorPrivate(Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getDeclaredConstructor();

        return Modifier.isPrivate(constructor.getModifiers());
    }

    /**
     * Calls the private constructor
     *
     * @param clazz Class which constructor to call
     * @throws Exception
     * @see Constructor#setAccessible(boolean)
     * @see Constructor#newInstance(Object...)
     */
    public static void callPrivateConstructor(Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
