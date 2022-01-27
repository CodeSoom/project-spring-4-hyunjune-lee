package com.hylee.limitless.domain.card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@DisplayName("CardRepository 클래스")
class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @BeforeEach
    public void prepare() {
        cardRepository.deleteAll();
    }

    @Nested
    @DisplayName("save는")
    class Describe_save {

        @Nested
        @DisplayName("Card 객체가 주어지면")
        class Context_with_a_card{
            LocalDateTime now = LocalDateTime.now();
            final Card givenCard = Card.builder()
                    .question("question")
                    .answer("answer")
                    .build();

            @Test
            @DisplayName("주어진 객체를 생성날짜와 같이 저장하고, 저장된 객체를 리턴한다")
            void it_save_obj_with_created_date_and_returns_a_saved_obj(){
                assertNull(givenCard.getId(), "저장되지 않은 객체는 아이다가 null 이다");

                final Card saved = cardRepository.save(givenCard);

                assertNotNull(saved.getId(), "저장된 객체는 아이디가 추가되어 있다");
                assertThat(saved.getCreatedDate()).isAfter(now);
            }
        }
    }
}
