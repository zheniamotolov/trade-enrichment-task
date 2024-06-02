package com.verygoodbank.tes.exception;

public class TradeEnrichmentException extends RuntimeException {
    public TradeEnrichmentException(String message) {
        super(message);
    }

    public TradeEnrichmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
