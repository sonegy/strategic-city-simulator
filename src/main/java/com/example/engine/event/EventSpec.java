package com.example.engine.event;

import com.example.domain.CategoryType;

import java.util.Map;

/**
 * 이벤트 정의 모델.
 * <p>
 * - name/type: 이벤트 식별 및 분류용 메타데이터
 * - probability: 0.0~1.0 사이의 발생 확률(빈도 스케일과 곱해져 최종 판단)
 * - condition: 발생 조건(현재 점수 기반)
 * - impact: 발생 시 적용할 카테고리별 점수 변화량(정수 델타)
 */
public record EventSpec(
        String name,
        String type,
        double probability,
        Condition condition,
        Map<CategoryType, Integer> impact
) {
    public EventSpec {
        if (probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException("probability must be [0,1]");
        }
        impact = Map.copyOf(impact);
    }
}
