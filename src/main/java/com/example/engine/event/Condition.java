package com.example.engine.event;

import com.example.domain.CategoryType;

import java.util.Map;

/**
 * 이벤트 발생 여부를 판정하기 위한 조건 인터페이스.
 * 구현체는 현재 카테고리 점수를 입력받아 true/false를 반환합니다.
 */
public interface Condition {
    /**
     * 현재 카테고리 점수 맵에 대해 조건을 평가합니다.
     */
    boolean evaluate(Map<CategoryType, Integer> currentScores);

    /**
     * 단일 카테고리에 대한 임계치 비교 조건을 생성합니다.
     */
    static Condition threshold(CategoryType category, ComparisonOperator op, int value) {
        return new ThresholdCondition(category, op, value);
    }

    /**
     * 두 조건의 논리 AND를 구성합니다.
     */
    static Condition and(Condition a, Condition b) {
        return scores -> a.evaluate(scores) && b.evaluate(scores);
    }
}
