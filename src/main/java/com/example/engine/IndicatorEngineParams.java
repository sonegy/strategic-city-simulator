package com.example.engine;

import com.example.domain.CategoryType;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * IndicatorEngine 계산에 사용되는 파라미터 집합.
 * Java 21 record로 불변성을 보장한다.
 */
public record IndicatorEngineParams(
        Map<CategoryType, Double> naturalDrift,           // 자연 변화(매 턴 가/감점)
        Map<CategoryType, Double> investmentEfficiency,   // 투자 효율(카테고리별 계수)
        Map<CategoryType, Map<CategoryType, Double>> interactionMatrix, // from->to 영향도(-3~+3 권장)
        double investmentBaseDelta,                       // 100% 투자 시 기본 변화량(점)
        double interactionScale                           // 상호작용 스케일(점)
) {
    public IndicatorEngineParams {
        naturalDrift = immutableEnumMap(naturalDrift);
        investmentEfficiency = immutableEnumMap(investmentEfficiency);
        interactionMatrix = immutableMatrix(interactionMatrix);
    }

    private static Map<CategoryType, Double> immutableEnumMap(Map<CategoryType, Double> src) {
        Objects.requireNonNull(src, "naturalDrift/investmentEfficiency는 null일 수 없습니다");
        EnumMap<CategoryType, Double> copy = new EnumMap<>(CategoryType.class);
        copy.putAll(src);
        return Map.copyOf(copy);
    }

    private static Map<CategoryType, Map<CategoryType, Double>> immutableMatrix(
            Map<CategoryType, Map<CategoryType, Double>> src
    ) {
        Objects.requireNonNull(src, "interactionMatrix는 null일 수 없습니다");
        EnumMap<CategoryType, Map<CategoryType, Double>> outer = new EnumMap<>(CategoryType.class);
        src.forEach((from, inner) -> {
            EnumMap<CategoryType, Double> innerCopy = new EnumMap<>(CategoryType.class);
            innerCopy.putAll(inner);
            outer.put(from, Map.copyOf(innerCopy));
        });
        return Map.copyOf(outer);
    }
}
