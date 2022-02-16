package com.hylee.limitless.errors;

public class CardSetNotFoundException extends Throwable {
    public CardSetNotFoundException(Long id) {
        super("CardSet not found: " + id);
    }
}
