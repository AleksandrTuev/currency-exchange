package com.dev.exception;

public class DaoException extends RuntimeException {
    public DaoException(Throwable throwable) {
        super(throwable);
    }

    public DaoException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
