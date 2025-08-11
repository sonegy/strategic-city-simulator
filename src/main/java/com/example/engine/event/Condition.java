package com.example.engine.event;

import com.example.domain.CategoryType;

import java.util.Map;

public interface Condition {
    boolean evaluate(Map<CategoryType, Integer> currentScores);

    static Condition threshold(CategoryType category, ComparisonOperator op, int value) {
        return new ThresholdCondition(category, op, value);
    }

    static Condition and(Condition a, Condition b) {
        return scores -> a.evaluate(scores) && b.evaluate(scores);
    }
}

