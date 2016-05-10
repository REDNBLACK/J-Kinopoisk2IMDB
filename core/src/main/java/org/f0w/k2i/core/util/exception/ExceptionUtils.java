package org.f0w.k2i.core.util.exception;

import java.util.function.Supplier;

public final class ExceptionUtils {
    private ExceptionUtils() {
    }

    /**
     * Wraps all checked exceptions in supplier into unchecked.
     *
     * @param supplier Supplier whom exceptions should be wrapped as unchecked
     * @param <T>      Type of supplier result
     * @param <E>      Type of exception
     * @return Result of supplier execution
     */
    public static <T, E extends Exception> T uncheck(SupplierWithExceptions<T, E> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throwAsUnchecked(e);
            return null;
        }
    }

    /**
     * Rethrows all checked exceptions inside a supplier.
     *
     * @param supplier Supplier which exceptions should be rethrown
     * @param <T> Type of supplier result
     * @param <E> Type of exception
     * @return Supplier
     */
    public static <T, E extends Exception> Supplier<T> rethrowSupplier(SupplierWithExceptions<T, E> supplier) {
        return () -> {
            try {
                return supplier.get();
            } catch (Exception exception) {
                throwAsUnchecked(exception);
                return null;
            }
        };
    }

    /**
     * Defines a supplier which can throw exceptions
     *
     * @param <T> the type of results supplied by this supplier
     * @see java.util.function.Supplier
     */
    @FunctionalInterface
    public interface SupplierWithExceptions<T, E extends Exception> {
        /**
         * Gets a result.
         *
         * @return a result
         * @throws Exception
         */
        T get() throws E;
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void throwAsUnchecked(Exception exception) throws E {
        throw (E) exception;
    }
}
