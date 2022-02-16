package com.hylee.limitless.controllers;

import com.hylee.limitless.application.CardSetService;
import com.hylee.limitless.domain.cardset.CardSet;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/sets")
@CrossOrigin
public class CardSetController {

    private final CardSetService cardSetService;

    public CardSetController(CardSetService cardSetService) {
        this.cardSetService = cardSetService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardSet create(@RequestBody @Valid CardSet cardSet) {
        return cardSetService.createCard(cardSet);
    }

}
