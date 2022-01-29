package com.hylee.limitless.application;

import com.hylee.limitless.domain.card.Card;
import com.hylee.limitless.domain.card.CardRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getCards() {
        return cardRepository.findAll();
    }
}
