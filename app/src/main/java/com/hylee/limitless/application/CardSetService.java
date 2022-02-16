package com.hylee.limitless.application;

import com.hylee.limitless.domain.cardset.CardSet;
import com.hylee.limitless.domain.cardset.CardSetRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CardSetService {
    
    private final CardSetRepository cardSetRepository;

    public CardSetService(CardSetRepository cardSetRepository) {
        this.cardSetRepository = cardSetRepository;
    }

    public CardSet createCard(CardSet cardSet) {
        return cardSetRepository.save(cardSet);
    }
}
