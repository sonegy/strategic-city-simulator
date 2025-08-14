package com.example.web.dto;

import com.example.domain.CategoryType;
import com.example.domain.Difficulty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

public class StartSessionResponse {
    @Schema(description = "생성된 세션 ID", example = "1")
    private Long sessionId;
    @Schema(description = "선택 난이도", example = "EASY")
    private Difficulty difficulty;
    @Schema(description = "초기 카테고리 점수(0~100)")
    private Map<CategoryType, Integer> scores;
    @Schema(description = "난이도에 따른 초기 예산(원)")
    private Long initialBudget;
    @Schema(description = "현재 금고 잔액(원)")
    private Long treasury;

    public StartSessionResponse(Long sessionId, Difficulty difficulty, Map<CategoryType, Integer> scores,
                                Long initialBudget, Long treasury) {
        this.sessionId = sessionId;
        this.difficulty = difficulty;
        this.scores = scores;
        this.initialBudget = initialBudget;
        this.treasury = treasury;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Map<CategoryType, Integer> getScores() {
        return scores;
    }

    public Long getInitialBudget() {
        return initialBudget;
    }

    public Long getTreasury() {
        return treasury;
    }
}
