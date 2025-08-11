package com.example.engine.event;

import com.example.domain.CategoryType;

import java.util.Map;

/**
 * 특정 카테고리 점수에 대해 임계 비교(>, ≥, <, ≤, ==, !=)를 수행하는 기본 조건 구현.
 */
class ThresholdCondition implements Condition {
    private final CategoryType category;
    private final ComparisonOperator op;
    private final int value;

    ThresholdCondition(CategoryType category, ComparisonOperator op, int value) {
        this.category = category;
        this.op = op;
        this.value = value;
    }

    @Override
    public boolean evaluate(Map<CategoryType, Integer> currentScores) {
        int current = currentScores.getOrDefault(category, 0);
        return switch (op) {
            case LT -> current < value;
            case LE -> current <= value;
            case GT -> current > value;
            case GE -> current >= value;
            case EQ -> current == value;
            case NE -> current != value;
        };
    }
}
