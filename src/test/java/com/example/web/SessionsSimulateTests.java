package com.example.web;

import com.example.domain.CategoryType;
import com.example.domain.Difficulty;
import com.example.repository.CategoryScoreRepository;
import com.example.repository.GameSessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.EnumMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SessionsSimulateTests {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired GameSessionRepository sessionRepository;
    @Autowired CategoryScoreRepository scoreRepository;

    long sessionId;

    @BeforeEach
    void setup() throws Exception {
        // 세션 생성 API 호출
        String payload = objectMapper.writeValueAsString(Map.of("difficulty", Difficulty.EASY));
        String location = mockMvc.perform(post("/api/v1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        assert location != null;
        sessionId = Long.parseLong(location.substring(location.lastIndexOf('/') + 1));
    }

    @Test
    void simulate_returns_delta_events_overallIndex() throws Exception {
        EnumMap<CategoryType, Double> ratios = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) ratios.put(c, 0.0);
        ratios.put(CategoryType.ECONOMY, 0.5);
        ratios.put(CategoryType.POLITICS, 0.2);

        String payload = objectMapper.writeValueAsString(Map.of("budgetRatios", ratios));

        mockMvc.perform(post("/api/v1/sessions/" + sessionId + "/simulate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.before.DEFENSE", is(50)))
                .andExpect(jsonPath("$.after.DEFENSE", any(Number.class)))
                .andExpect(jsonPath("$.delta.ECONOMY", any(Number.class)))
                .andExpect(jsonPath("$.events", isA(java.util.List.class)))
                .andExpect(jsonPath("$.overallIndex", greaterThan(0.0)));
    }
}

