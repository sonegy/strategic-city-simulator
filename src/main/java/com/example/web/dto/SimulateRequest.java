package com.example.web.dto;

import com.example.domain.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public class SimulateRequest {
    @Schema(description = "카테고리별 예산 비율(0.0~1.0)")
    @NotNull @NotEmpty
    private Map<CategoryType, Double> budgetRatios;

    public Map<CategoryType, Double> getBudgetRatios() {
        return budgetRatios;
    }

    public void setBudgetRatios(Map<CategoryType, Double> budgetRatios) {
        this.budgetRatios = budgetRatios;
    }
}

