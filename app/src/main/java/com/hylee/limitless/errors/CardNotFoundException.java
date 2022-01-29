package com.hylee.limitless.errors;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(Long id) {
        super("Card not found: " + id);
    }
}
