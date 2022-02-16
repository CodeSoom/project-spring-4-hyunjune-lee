package com.hylee.limitless.domain.cardset;

import com.hylee.limitless.domain.card.Card;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Getter
@Builder
public class CardSet {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String name;

    @OneToMany
    private List<Card> cardList;

    public CardSet() {
    }

    public CardSet(Long id, String name, List<Card> cardList) {
        this.id = id;
        this.name = name;
        this.cardList = cardList;
    }
}
