package com.hylee.limitless.application;

import com.hylee.limitless.domain.card.Card;
import com.hylee.limitless.domain.card.CardRepository;
import com.hylee.limitless.errors.CardNotFoundException;
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

    public Card createCard(Card card) {
        return cardRepository.save(card);
    }

    public Card getCard(Long id) {
        return cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
    }

    public Card updateCard(Long id, Card source) {
        Card target = getCard(id);
        target.changeWith(source);

        return target;
    }
}
