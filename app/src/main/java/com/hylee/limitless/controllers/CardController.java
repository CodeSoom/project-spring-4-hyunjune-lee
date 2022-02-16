package com.hylee.limitless.controllers;

import com.hylee.limitless.application.CardService;
import com.hylee.limitless.domain.card.Card;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cards")
@CrossOrigin
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Card create(@RequestBody @Valid Card card) {
        return cardService.createCard(card);
    }

    @GetMapping
    public List<Card> list() {
        return cardService.getCards();
    }

    @GetMapping("{id}")
    public Card detail(@PathVariable Long id) {
        return cardService.getCard(id);
    }

    @PatchMapping("{id}")
    public Card update(@PathVariable Long id,
                       @RequestBody @Valid Card card) {
        return cardService.updateCard(id, card);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Card delete(@PathVariable Long id) {
        return cardService.deleteCard(id);
    }

}
