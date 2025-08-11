package com.example.web;

import com.example.domain.CategoryType;
import com.example.domain.Difficulty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.EnumMap;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
class OpenApiAndDocsTests {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void swagger_ui_and_openapi_docs_exposed() throws Exception {
        // OpenAPI JSON
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi", notNullValue()))
                .andDo(document("openapi-docs"));
    }

    @Test
    void generate_basic_snippets_for_main_endpoints() throws Exception {
        // 1) Start session
        String startPayload = objectMapper.writeValueAsString(Map.of("difficulty", Difficulty.EASY));
        var startRes = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(startPayload))
                .andExpect(status().isCreated())
                .andDo(document("sessions-start"))
                .andReturn();
        String location = startRes.getResponse().getHeader("Location");
        assert location != null;
        long sessionId = Long.parseLong(location.substring(location.lastIndexOf('/') + 1));

        // 2) Simulate month
        EnumMap<CategoryType, Double> ratios = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) ratios.put(c, 0.0);
        ratios.put(CategoryType.ECONOMY, 0.5);
        ratios.put(CategoryType.POLITICS, 0.2);
        String simulatePayload = objectMapper.writeValueAsString(Map.of("budgetRatios", ratios));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/sessions/{id}/simulate", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(simulatePayload))
                .andExpect(status().isOk())
                .andDo(document("sessions-simulate"));

        // 3) Report
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/sessions/{id}/reports", sessionId))
                .andExpect(status().isOk())
                .andDo(document("sessions-report"));
    }
}

