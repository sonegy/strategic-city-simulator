package com.example.engine;

import com.example.domain.CategoryType;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * IndicatorEngine 계산에 사용되는 파라미터 집합.
 */
public class IndicatorEngineParams {
    private final Map<CategoryType, Double> naturalDrift;           // 자연 변화(매 턴 가/감점)
    private final Map<CategoryType, Double> investmentEfficiency;   // 투자 효율(카테고리별 계수)
    private final Map<CategoryType, Map<CategoryType, Double>> interactionMatrix; // from->to 영향도(-3~+3 권장)
    private final double investBaseDelta;                           // 100% 투자 시 기본 변화량(점)
    private final double interactionScale;                          // 상호작용 스케일(점)

    public IndicatorEngineParams(
            Map<CategoryType, Double> naturalDrift,
            Map<CategoryType, Double> investmentEfficiency,
            Map<CategoryType, Map<CategoryType, Double>> interactionMatrix,
            double investBaseDelta,
            double interactionScale
    ) {
        this.naturalDrift = immutableEnumMap(naturalDrift);
        this.investmentEfficiency = immutableEnumMap(investmentEfficiency);
        this.interactionMatrix = immutableMatrix(interactionMatrix);
        this.investBaseDelta = investBaseDelta;
        this.interactionScale = interactionScale;
    }

    public Map<CategoryType, Double> naturalDrift() { return naturalDrift; }
    public Map<CategoryType, Double> investmentEfficiency() { return investmentEfficiency; }
    public Map<CategoryType, Map<CategoryType, Double>> interactionMatrix() { return interactionMatrix; }
    public double investBaseDelta() { return investBaseDelta; }
    public double interactionScale() { return interactionScale; }

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

