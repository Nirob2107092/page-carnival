package com.pc.pc.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String bookTitle) {
        super("Insufficient stock for: \"" + bookTitle + "\"");
    }
}
