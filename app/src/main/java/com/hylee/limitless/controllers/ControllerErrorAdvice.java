package com.hylee.limitless.controllers;

import com.hylee.limitless.dto.ErrorResponse;
import com.hylee.limitless.errors.CardNotFoundException;
import com.hylee.limitless.errors.CardSetNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ControllerAdvice
public class ControllerErrorAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CardNotFoundException.class)
    public ErrorResponse handleCardNotFound() {
        return new ErrorResponse("Card not found");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CardSetNotFoundException.class)
    public ErrorResponse handleCardSetNotFound() {
        return new ErrorResponse("CardSet not found");
    }

}
