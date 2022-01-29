package com.hylee.limitless.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hylee.limitless.application.CardService;
import com.hylee.limitless.domain.card.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
@AutoConfigureMockMvc
@DisplayName("CardController 클래스는")
@MockBean(JpaMetamodelMappingContext.class)
class CardControllerTest {
    private ObjectMapper objectMapper = new ObjectMapper();

    String cardContent;
    Card card;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        card = Card.builder()
                .question("question")
                .answer("answer")
                .build();
        given(cardService.getCards()).willReturn(List.of(card));
        cardContent = objectMapper.writeValueAsString(card);
    }

    @Nested
    @DisplayName("GET /cards는")
    class Describe_request_get_to_cards_path {

        @Test
        @DisplayName("저장되어 있는 card 리스트를 응답합니다.")
        void it_responses_card_list() throws Exception {
            mockMvc.perform(get("/cards"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("question")));
        }
    }

}
