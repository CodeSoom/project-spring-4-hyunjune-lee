package com.hylee.limitless.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
@AutoConfigureMockMvc
@DisplayName("CardController 클래스는")
@MockBean(JpaMetamodelMappingContext.class)
class CardControllerTest {
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    String cardContent;
    Card card;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        card = Card.builder()
                .id(0L)
                .question("question")
                .answer("answer")
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
        given(cardService.getCards()).willReturn(List.of(card));
        cardContent = objectMapper.writeValueAsString(card);
    }

    @Nested
    @DisplayName("GET /cards")
    class Describe_request_get_to_cards_path {

        @Test
        @DisplayName("저장되어 있는 card 리스트를 응답합니다.")
        void it_responses_card_list() throws Exception {
            mockMvc.perform(get("/cards"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("question")));
        }
    }

    @Nested
    @DisplayName("POST /cards")
    //TODO 유효한 속성하고 유효하지 않은 속성으로 나누기 -> CardDto 만들고 @Valid 추가해서 진행
    class Describe_request_post_to_cards_id_path{

        @Nested
        @DisplayName("유효한 속성을 가진 card가 주어지면")
        class Context_with_a_valid_attributes{

            @BeforeEach
            void setUp(){
                given(cardService.createCard(any(Card.class))).willReturn(card);
            }

            @Test
            @DisplayName("생성한 card를 응답합니다.")
            void it_responses_created_card() throws Exception {
                mockMvc.perform(post("/cards")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(cardContent))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(cardContent));
            }
        }
    }

    @Nested
    @DisplayName("GET /cards/{id}")
    class Describe_request_get_to_cards_id_path{

        @Nested
        @DisplayName("만약 조회하는 id의 card가 존재한다면")
        class Context_with_exist_id {
            private final Long existId = 0L;

            @BeforeEach
            void setUp(){
                given(cardService.getCard(existId)).willReturn(card);
            }

            @Test
            @DisplayName("해당하는 card를 응답합니다.")
            void it_responses_card_with_exist_id() throws Exception {
                mockMvc.perform(
                        get("/cards/0")
                                .accept(MediaType.APPLICATION_JSON)
                )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("question")));
            }
        }

    }


}
