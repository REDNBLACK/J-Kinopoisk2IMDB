package org.f0w.k2i.core.util.exception;

/**
 * Kinopoisk2IMDB app domain exception. Must be used in place of {@link RuntimeException} wrapping.
 */
public class KinopoiskToIMDBException extends RuntimeException {
    /** {@inheritDoc} */
    public KinopoiskToIMDBException() {}

    /** {@inheritDoc} */
    public KinopoiskToIMDBException(String message) {
        super(message);
    }

    /** {@inheritDoc} */
    public KinopoiskToIMDBException(String message, Throwable cause) {
        super(message, cause);
    }

    /** {@inheritDoc} */
    public KinopoiskToIMDBException(Throwable cause) {
        super(cause);
    }

    /** {@inheritDoc} */
    public KinopoiskToIMDBException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
