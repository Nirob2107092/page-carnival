package com.pc.pc.exception;

public class EmptyCartException extends RuntimeException {

    public EmptyCartException() {
        super("Cannot place order: your cart is empty.");
    }
}
