package com.hylee.limitless.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hylee.limitless.application.CardSetService;
import com.hylee.limitless.domain.card.Card;
import com.hylee.limitless.domain.cardset.CardSet;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardSetController.class)
@AutoConfigureMockMvc
@DisplayName("CardSetController 클래스는")
@MockBean(JpaMetamodelMappingContext.class)
class CardSetControllerTest {
    private final Long existId = 0L;
    private final Long notExistId = 1000L;
    private CardSet cardSet;
    private String cardSetContent;
    private Card card1;
    private Card card2;
    private CardSet invalidCardSet;
    private String invalidCardSetContent;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardSetService cardSetService;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        cardSet = CardSet.builder()
                .id(existId)
                .name("data structure")
                .build();
        cardSetContent = objectMapper.writeValueAsString(cardSet);

        invalidCardSet = CardSet.builder()
                .id(existId)
                .name("")
                .build();
        invalidCardSetContent = objectMapper.writeValueAsString(invalidCardSet);

        given(cardSetService.createCard(any(CardSet.class))).willReturn(cardSet);

    }

    //[Create]
    @Nested
    @DisplayName("POST /sets")
    class Describe_request_post_to_sets_path {

        @Nested
        @DisplayName("유효한 속성을 가진 set이 주어지면")
        class Context_with_a_valid_attributes {

            @Test
            @DisplayName("생성한 set를 응답합니다.")
            void it_responses_created_set() throws Exception {
                mockMvc.perform(post("/sets")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(cardSetContent))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(cardSetContent));
            }
        }

        @Nested
        @DisplayName("유효하지 않은 속성을 가진 set이 주어지면")
        class Context_with_a_invalid_attributes {

            @Test
            @DisplayName("Bad_Request(400)를 응답합니다.")
            void it_responses_bad_request() throws Exception {
                mockMvc.perform(post("/sets")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidCardSetContent))
                        .andExpect(status().isBadRequest());
            }
        }
    }

//    //[Read]
//    @Nested
//    @DisplayName("GET /sets")
//    class Describe_request_get_to_sets_path {
//
//        @Test
//        @DisplayName("저장되어 있는 card set 리스트를 응답합니다.")
//        void it_responses_card_list() throws Exception {
//            mockMvc.perform(get("/sets"))
//                    .andExpect(status().isOk())
//                    .andExpect(content().string(containsString("stack")));
//        }
//    }

}
