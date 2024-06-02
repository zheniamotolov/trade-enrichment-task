package com.verygoodbank.tes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TradeEnrichmentException.class)
    public ResponseEntity<String> handleTradeValidationException(TradeEnrichmentException e) {
        return new ResponseEntity<>("Failed to enrich trade data", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
