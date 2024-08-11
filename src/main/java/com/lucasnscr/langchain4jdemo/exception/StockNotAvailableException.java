package com.lucasnscr.langchain4jdemo.exception;

import org.springframework.http.HttpStatus;

public class StockNotAvailableException extends RuntimeException {
    public StockNotAvailableException(HttpStatus httpStatus, String message) {
        super();
    }
}
