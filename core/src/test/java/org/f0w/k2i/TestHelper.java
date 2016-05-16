package org.f0w.k2i;

import com.google.common.io.Resources;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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

    /**
     * Gets contents of resource from resources dir
     *
     * @param resource Resource to get contents from
     * @return Resource contents
     * @throws IOException
     */
    public static String getResourceContents(final String resource) throws IOException {
        return getResourceContents(resource, StandardCharsets.UTF_8);
    }

    /**
     * Gets contents of resource from resources dir.
     *
     * @param resource Resource to get contents from
     * @param charset Charset to read contents with
     * @return Resource contents
     * @throws IOException
     */
    public static String getResourceContents(final String resource, Charset charset) throws IOException {
        return Resources.toString(TestHelper.class.getClassLoader().getResource(resource), charset);
    }
}
