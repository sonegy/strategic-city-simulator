package com.example.engine;

import com.example.domain.CategoryType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class InteractionMatrixLoaderTests {

    @Test
    void load_sample_matrix_and_apply_in_engine() {
        InteractionMatrixLoader loader = new InteractionMatrixLoader(new ObjectMapper());
        Map<CategoryType, Map<CategoryType, Double>> matrix = loader.loadFromClasspath("/interaction-matrix.sample.json");

        // 매트릭스 일부 값 확인
        assertThat(matrix.get(CategoryType.POLITICS).get(CategoryType.ECONOMY)).isEqualTo(2.0);
        assertThat(matrix.get(CategoryType.ECONOMY).get(CategoryType.ENVIRONMENT)).isEqualTo(-2.0);

        // 엔진에 적용 테스트
        IndicatorEngine engine = new BasicIndicatorEngine();

        EnumMap<CategoryType, Integer> current = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) current.put(c, 50);

        EnumMap<CategoryType, Double> ratios = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) ratios.put(c, 0.0);
        ratios.put(CategoryType.ECONOMY, 0.5);
        ratios.put(CategoryType.POLITICS, 0.2);

        EnumMap<CategoryType, Double> natural = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) natural.put(c, 0.0);

        EnumMap<CategoryType, Double> eff = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) eff.put(c, 1.0);

        IndicatorEngineParams params = new IndicatorEngineParams(
                natural,
                eff,
                matrix,
                10.0,
                1.0
        );

        Map<CategoryType, Integer> next = engine.updateCategoryScores(current, ratios, params);

        // BasicIndicatorEngineTests와 동일한 시나리오 검증
        assertThat(next.get(CategoryType.ECONOMY)).isEqualTo(55);      // 50 + 5 + 0.4 -> 55.4 -> 55
        assertThat(next.get(CategoryType.ENVIRONMENT)).isEqualTo(49);  // 50 + 0 + (-1.0) -> 49
    }
}

