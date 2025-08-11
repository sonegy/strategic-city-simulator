package com.example.engine;

import com.example.domain.CategoryType;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * 투자효율/자연변화/상호작용 파라미터를 적용하는 기본 구현.
 * 반올림 정책: 계산은 double로 수행하고, 최종 결과를 Math.round로 정수화한 뒤 0~100으로 클램프한다.
 */
public class BasicIndicatorEngine implements IndicatorEngine {

    @Override
    public Map<CategoryType, Integer> updateCategoryScores(
            Map<CategoryType, Integer> currentScores,
            Map<CategoryType, Double> budgetRatios,
            IndicatorEngineParams params
    ) {
        Objects.requireNonNull(currentScores, "currentScores는 null일 수 없습니다");
        Objects.requireNonNull(budgetRatios, "budgetRatios는 null일 수 없습니다");
        Objects.requireNonNull(params, "params는 null일 수 없습니다");

        // 입력 검증: 비율은 [0.0, 1.0] 범위
        for (Map.Entry<CategoryType, Double> e : budgetRatios.entrySet()) {
            Double v = e.getValue();
            if (v == null || v.isNaN() || v < 0.0 || v > 1.0) {
                throw new IllegalArgumentException("예산 비율은 0.0~1.0 이어야 합니다: " + e.getKey() + "=" + v);
            }
        }

        EnumMap<CategoryType, Integer> result = new EnumMap<>(CategoryType.class);
        for (CategoryType to : CategoryType.values()) {
            int current = currentScores.getOrDefault(to, 50);

            double ratioTo = budgetRatios.getOrDefault(to, 0.0);
            double effTo = params.investmentEfficiency().getOrDefault(to, 1.0);
            double natural = params.naturalDrift().getOrDefault(to, 0.0);

            double investDelta = params.investmentBaseDelta() * ratioTo * effTo;

            // 상호작용: 각 from 카테고리의 예산 비율이 to에 미치는 영향의 합
            double interactionSum = 0.0;
            for (CategoryType from : CategoryType.values()) {
                double ratioFrom = budgetRatios.getOrDefault(from, 0.0);
                double weight = params.interactionMatrix()
                        .getOrDefault(from, Map.of())
                        .getOrDefault(to, 0.0);
                interactionSum += ratioFrom * weight;
            }
            double interactionDelta = interactionSum * params.interactionScale();

            double next = current + natural + investDelta + interactionDelta;
            int clamped = clampToInt(Math.round(next), 0, 100);
            result.put(to, clamped);
        }
        return result;
    }

    private static int clampToInt(long value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return (int) value;
    }
}
