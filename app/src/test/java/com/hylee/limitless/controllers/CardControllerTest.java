package com.hylee.limitless.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hylee.limitless.application.CardService;
import com.hylee.limitless.domain.card.Card;
import com.hylee.limitless.errors.CardNotFoundException;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
@AutoConfigureMockMvc
@DisplayName("CardController 클래스는")
@MockBean(JpaMetamodelMappingContext.class)
class CardControllerTest {
    private final Long existId = 0L;
    private final Long notExistId = 1000L;
    private Card card;
    private String cardContent;
    private Card updatedCard;
    private String updatedCardContent;
    private Card invalidCard;
    private String invalidCardContent;
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @BeforeEach
    void setUp() throws JsonProcessingException {

        card = Card.builder()
                .id(existId)
                .question("What is the data structure using LIFO approach?")
                .answer("stack")
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
        cardContent = objectMapper.writeValueAsString(card);

        updatedCard = Card.builder()
                .id(existId)
                .question("What is the data structure using FIFO approach?")
                .answer("queue")
                .createdDate(card.getCreatedDate())
                .modifiedDate(LocalDateTime.now())
                .build();
        updatedCardContent = objectMapper.writeValueAsString(updatedCard);

        invalidCard = Card.builder()
                .id(existId)
                .question("")
                .answer("answer")
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
        invalidCardContent = objectMapper.writeValueAsString(invalidCard);

        given(cardService.getCards()).willReturn(List.of(card));

        given(cardService.createCard(any(Card.class))).willReturn(card);

        given(cardService.getCard(existId)).willReturn(card);

        given(cardService.getCard(notExistId)).willThrow(new CardNotFoundException(notExistId));

        given(cardService.updateCard(eq(existId), any(Card.class)))
                .will(invocation -> {
                    Long id = invocation.getArgument(0);
                    Card card = invocation.getArgument(1);
                    return Card.builder()
                            .id(id)
                            .question(card.getQuestion())
                            .answer(card.getAnswer())
                            .build();
                });

        given(cardService.updateCard(eq(notExistId), any(Card.class))).willThrow(new CardNotFoundException(notExistId));

        given(cardService.deleteCard(notExistId)).willThrow(new CardNotFoundException(notExistId));

    }

    //[Create]
    @Nested
    @DisplayName("POST /cards")
    class Describe_request_post_to_cards_path {

        @Nested
        @DisplayName("유효한 속성을 가진 card가 주어지면")
        class Context_with_a_valid_attributes {

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

        @Nested
        @DisplayName("유효하지 않은 속성을 가진 card가 주어지면")
        class Context_with_a_invalid_attributes {

            @Test
            @DisplayName("Bad_Request(400)를 응답합니다.")
            void it_responses_bad_request() throws Exception {
                mockMvc.perform(post("/cards")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidCardContent))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    //[Read]
    @Nested
    @DisplayName("GET /cards")
    class Describe_request_get_to_cards_path {

        @Test
        @DisplayName("저장되어 있는 card 리스트를 응답합니다.")
        void it_responses_card_list() throws Exception {
            mockMvc.perform(get("/cards"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("stack")));
        }
    }

    //[Read]
    @Nested
    @DisplayName("GET /cards/{id}")
    class Describe_request_get_to_cards_id_path {

        @Nested
        @DisplayName("만약 조회하는 id의 card가 존재한다면")
        class Context_with_exist_id {

            @Test
            @DisplayName("해당하는 card를 응답합니다.")
            void it_responses_card_with_exist_id() throws Exception {
                mockMvc.perform(
                                get("/cards/" + existId)
                                        .accept(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("stack")));
            }
        }

        @Nested
        @DisplayName("만약 조회하는 id의 card가 존재하지 않는다면")
        class Context_with_not_exist_id {

            @Test
            @DisplayName("Not_Found(404)를 응답합니다.")
            void it_responses_not_found() throws Exception {
                mockMvc.perform(
                                get("/cards/" + notExistId)
                                        .accept(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isNotFound());
            }
        }
    }

    //[Update]
    @Nested
    @DisplayName("PATCH /cards/{id}")
    class Describe_request_patch_to_cards_id_path {

        @Nested
        @DisplayName("만약 조회하는 id가 존재하고 유효한 속성을 가진 card가 주어지면")
        class Context_with_exist_id_and_valid_attributes {

            @Test
            @DisplayName("수정된 card를 응답합니다.")
            void it_responses_updated_card() throws Exception {
                mockMvc.perform(patch("/cards/" + existId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatedCardContent))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("queue")));
            }
        }

        @Nested
        @DisplayName("만약 조회하는 id가 존재하지 않고 유효한 속성을 가진 card가 주어지면")
        class Context_with_not_exist_id_and_valid_attributes {

            @Test
            @DisplayName("Not_Found(404)를 응답합니다.")
            void it_responses_not_found() throws Exception {
                mockMvc.perform(patch("/cards/" + notExistId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatedCardContent))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 속성을 가진 card가 주어지면")
        class Context_with_a_invalid_attributes {

            @Test
            @DisplayName("Bad_Request(400)를 응답합니다.")
            void it_responses_bad_request() throws Exception {
                mockMvc.perform(patch("/cards/" + existId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidCardContent))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    //[Delete]
    @Nested
    @DisplayName("Delete /cards/{id}")
    class Describe_request_delete_to_cards_id_path {

        @Nested
        @DisplayName("만약 조회하는 id의 card가 존재한다면")
        class Context_with_exist_id {

            @Test
            @DisplayName("No_Content(204)를 응답합니다.")
            void it_responses_no_content() throws Exception {
                mockMvc.perform(
                                delete("/cards/" + existId)
                                        .accept(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("만약 조회하는 id의 card가 존재하지 않는다면")
        class Context_with_not_exist_id {

            @Test
            @DisplayName("Not_Found(404)를 응답합니다.")
            void it_responses_not_found() throws Exception {
                mockMvc.perform(
                                delete("/cards/" + notExistId)
                                        .accept(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isNotFound());
            }
        }
    }
}
