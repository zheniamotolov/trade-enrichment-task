package com.verygoodbank.tes.exception;

public class ProductInitializationException extends RuntimeException {
    public ProductInitializationException(String message) {
        super(message);
    }

    public ProductInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
