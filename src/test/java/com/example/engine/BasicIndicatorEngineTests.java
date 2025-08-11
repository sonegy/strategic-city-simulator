package com.example.engine;

import com.example.domain.CategoryType;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class BasicIndicatorEngineTests {

    private static IndicatorEngineParams params(
            double investBaseDelta,
            double interactionScale,
            Map<CategoryType, Double> natural,
            Map<CategoryType, Double> eff,
            Map<CategoryType, Map<CategoryType, Double>> mat
    ) {
        return new IndicatorEngineParams(natural, eff, mat, investBaseDelta, interactionScale);
    }

    private static Map<CategoryType, Double> zerosDouble() {
        EnumMap<CategoryType, Double> m = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) m.put(c, 0.0);
        return m;
    }

    private static Map<CategoryType, Integer> all50() {
        EnumMap<CategoryType, Integer> m = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) m.put(c, 50);
        return m;
    }

    private static Map<CategoryType, Double> onesDouble() {
        EnumMap<CategoryType, Double> m = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) m.put(c, 1.0);
        return m;
    }

    private static Map<CategoryType, Map<CategoryType, Double>> zeroMatrix() {
        EnumMap<CategoryType, Map<CategoryType, Double>> outer = new EnumMap<>(CategoryType.class);
        for (CategoryType from : CategoryType.values()) {
            EnumMap<CategoryType, Double> inner = new EnumMap<>(CategoryType.class);
            for (CategoryType to : CategoryType.values()) inner.put(to, 0.0);
            outer.put(from, inner);
        }
        return outer;
    }

    @Test
    void investment_and_natural_drift_applied() {
        IndicatorEngine engine = new BasicIndicatorEngine();

        Map<CategoryType, Integer> current = all50();
        EnumMap<CategoryType, Double> ratios = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) ratios.put(c, 0.0);
        ratios.put(CategoryType.ECONOMY, 0.5); // 50% 투자

        // 자연 감소 -0.1, 투자효율 1.0, 상호작용 없음, 100% 투자 시 +10점
        EnumMap<CategoryType, Double> natural = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) natural.put(c, -0.1);
        IndicatorEngineParams p = params(10.0, 1.0, natural, onesDouble(), zeroMatrix());

        Map<CategoryType, Integer> next = engine.updateCategoryScores(current, ratios, p);

        // Economy: 50 + (-0.1) + (10 * 0.5 * 1.0) = 55. - rounding to 55
        assertThat(next.get(CategoryType.ECONOMY)).isEqualTo(55);
        // Others: 50 + (-0.1) + 0 = 50 (rounded)
        assertThat(next.get(CategoryType.DEFENSE)).isEqualTo(50);
    }

    @Test
    void interaction_matrix_applied() {
        IndicatorEngine engine = new BasicIndicatorEngine();

        Map<CategoryType, Integer> current = all50();
        EnumMap<CategoryType, Double> ratios = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) ratios.put(c, 0.0);
        ratios.put(CategoryType.ECONOMY, 0.5);
        ratios.put(CategoryType.POLITICS, 0.2);

        Map<CategoryType, Map<CategoryType, Double>> mat = zeroMatrix();
        // POLITICS -> ECONOMY : +2, ECONOMY -> ENVIRONMENT : -2
        ((Map<CategoryType, Double>) mat.get(CategoryType.POLITICS)).put(CategoryType.ECONOMY, 2.0);
        ((Map<CategoryType, Double>) mat.get(CategoryType.ECONOMY)).put(CategoryType.ENVIRONMENT, -2.0);

        IndicatorEngineParams p = params(10.0, 1.0, zerosDouble(), onesDouble(), mat);

        Map<CategoryType, Integer> next = engine.updateCategoryScores(current, ratios, p);

        // Economy: 50 + invest(5.0) + interaction(0.2*2=0.4) = 55.4 -> 55
        assertThat(next.get(CategoryType.ECONOMY)).isEqualTo(55);
        // Environment: 50 + invest(0) + interaction(0.5 * -2 = -1.0) = 49 -> 49
        assertThat(next.get(CategoryType.ENVIRONMENT)).isEqualTo(49);
    }

    @Test
    void clamped_to_range_0_100() {
        IndicatorEngine engine = new BasicIndicatorEngine();
        EnumMap<CategoryType, Integer> current = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) current.put(c, 99);

        EnumMap<CategoryType, Double> ratios = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) ratios.put(c, 1.0);

        IndicatorEngineParams p = params(10.0, 0.0, zerosDouble(), onesDouble(), zeroMatrix());

        Map<CategoryType, Integer> next = engine.updateCategoryScores(current, ratios, p);
        for (CategoryType c : CategoryType.values()) {
            assertThat(next.get(c)).isEqualTo(100);
        }
    }
}

