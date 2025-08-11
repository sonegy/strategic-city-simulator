package com.example.engine.event;

import com.example.domain.CategoryType;

import java.util.Map;

/**
 * 이벤트 정의: 이름/유형/발생확률/조건/효과(카테고리 델타)
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

