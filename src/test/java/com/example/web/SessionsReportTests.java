package com.example.web;

import com.example.domain.CategoryType;
import com.example.domain.Difficulty;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SessionsReportTests {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    long sessionId;

    @BeforeEach
    void setup() throws Exception {
        // 세션 생성
        String payload = objectMapper.writeValueAsString(Map.of("difficulty", Difficulty.NORMAL));
        String location = mockMvc.perform(post("/api/v1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        assert location != null;
        sessionId = Long.parseLong(location.substring(location.lastIndexOf('/') + 1));

        // 한 번 시뮬레이트하여 이벤트/점수 생성
        EnumMap<CategoryType, Double> ratios = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) ratios.put(c, 0.0);
        ratios.put(CategoryType.ECONOMY, 0.4);
        ratios.put(CategoryType.POLITICS, 0.3);
        String sim = objectMapper.writeValueAsString(Map.of("budgetRatios", ratios));
        mockMvc.perform(post("/api/v1/sessions/" + sessionId + "/simulate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sim))
                .andExpect(status().isOk());
    }

    @Test
    void reports_returns_scores_events_overall_and_cumulative() throws Exception {
        mockMvc.perform(get("/api/v1/sessions/" + sessionId + "/reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scores.DEFENSE", any(Number.class)))
                .andExpect(jsonPath("$.overallIndex", greaterThan(0.0)))
                .andExpect(jsonPath("$.events", isA(java.util.List.class)))
                .andExpect(jsonPath("$.cumulativeEventsByType", aMapWithSize(greaterThanOrEqualTo(0))));
    }
}

