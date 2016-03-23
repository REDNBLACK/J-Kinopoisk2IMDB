package org.f0w.k2i.core.utils.exception;

public class KinopoiskToIMDBException extends RuntimeException {
    public KinopoiskToIMDBException() {}

    public KinopoiskToIMDBException(String message) {
        super(message);
    }

    public KinopoiskToIMDBException(String message, Throwable cause) {
        super(message, cause);
    }

    public KinopoiskToIMDBException(Throwable cause) {
        super(cause);
    }

    public KinopoiskToIMDBException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
