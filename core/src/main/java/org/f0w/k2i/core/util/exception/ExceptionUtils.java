package org.f0w.k2i.core.util.exception;

public final class ExceptionUtils {
    private ExceptionUtils() {
    }

    /**
     * Wraps all checked exceptions in supplier into unchecked.
     *
     * @param supplier Supplier whom exceptions should be wrapped as unchecked
     * @param <T>      Type of supplier result
     * @return Result of supplier execution
     */
    public static <T> T uncheck(SupplierWithExceptions<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new KinopoiskToIMDBException(e);
        }
    }

    /**
     * Defines a supplier which can throw exceptions
     *
     * @param <T> the type of results supplied by this supplier
     * @see java.util.function.Supplier
     */
    @FunctionalInterface
    public interface SupplierWithExceptions<T> {
        /**
         * Gets a result.
         *
         * @return a result
         * @throws Exception
         */
        T get() throws Exception;
    }
}
