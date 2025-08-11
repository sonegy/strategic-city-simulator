package com.example.engine.event;

import com.example.domain.CategoryType;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.example.engine.event.ComparisonOperator.*;
import static com.example.engine.event.Condition.threshold;
import static org.assertj.core.api.Assertions.assertThat;

class EventEngineTests {

    private static Map<CategoryType, Integer> scores(int def, int dip, int eco, int pol, int cul, int env) {
        EnumMap<CategoryType, Integer> m = new EnumMap<>(CategoryType.class);
        m.put(CategoryType.DEFENSE, def);
        m.put(CategoryType.DIPLOMACY, dip);
        m.put(CategoryType.ECONOMY, eco);
        m.put(CategoryType.POLITICS, pol);
        m.put(CategoryType.CULTURE, cul);
        m.put(CategoryType.ENVIRONMENT, env);
        return m;
    }

    private static EventSpec ev(String name, String type, double p, Condition cond, Map<CategoryType, Integer> impact) {
        return new EventSpec(name, type, p, cond, impact);
    }

    @Test
    void simulate_five_sample_events_deterministically() {
        // 초기 점수: PRD 조건을 충족하도록 설정
        Map<CategoryType, Integer> current = scores(
                75, // DEFENSE > 70
                65, // DIPLOMACY > 60
                72, // ECONOMY > 70
                35, // POLITICS < 40
                55,
                45  // ENVIRONMENT < 50
        );

        // 샘플 5개 이벤트 (PRD 예시 기반, 확률은 테스트를 위해 1.0으로 설정)
        List<EventSpec> specs = List.of(
                ev("태풍 피해", "자연재해", 1.0,
                        threshold(CategoryType.ENVIRONMENT, LT, 50),
                        Map.of(CategoryType.ECONOMY, -5, CategoryType.ENVIRONMENT, -7)
                ),
                ev("무역 협정 체결", "외교", 1.0,
                        threshold(CategoryType.DIPLOMACY, GT, 60),
                        Map.of(CategoryType.ECONOMY, +5, CategoryType.DIPLOMACY, +3)
                ),
                ev("대규모 부패 스캔들", "정치", 1.0,
                        threshold(CategoryType.POLITICS, LT, 40),
                        Map.of(CategoryType.POLITICS, -10, CategoryType.DIPLOMACY, -5)
                ),
                ev("신기술 개발", "기술혁신", 1.0,
                        threshold(CategoryType.ECONOMY, GT, 70),
                        Map.of(CategoryType.ECONOMY, +8, CategoryType.ENVIRONMENT, +2)
                ),
                ev("군비 경쟁", "국방", 1.0,
                        threshold(CategoryType.DEFENSE, GT, 70),
                        Map.of(CategoryType.DEFENSE, +2, CategoryType.ECONOMY, -3, CategoryType.DIPLOMACY, -2)
                )
        );

        // 확률을 모두 통과하도록 nextDouble()=0.0을 반환하는 랜덤
        EventRandom rnd = () -> 0.0;

        EventEngine engine = new DefaultEventEngine();
        EventResult result = engine.applyEvents(current, specs, rnd, 1.0);

        // 효과 누적 계산:
        // ECONOMY: -5 + 5 + 8 - 3 = +5  => 72 + 5 = 77
        // ENVIRONMENT: -7 + 2 = -5      => 45 - 5 = 40
        // DIPLOMACY: +3 - 5 - 2 = -4    => 65 - 4 = 61
        // POLITICS: -10                 => 35 -10 = 25
        // DEFENSE: +2                   => 75 + 2 = 77
        assertThat(result.nextScores()).containsEntry(CategoryType.ECONOMY, 77);
        assertThat(result.nextScores()).containsEntry(CategoryType.ENVIRONMENT, 40);
        assertThat(result.nextScores()).containsEntry(CategoryType.DIPLOMACY, 61);
        assertThat(result.nextScores()).containsEntry(CategoryType.POLITICS, 25);
        assertThat(result.nextScores()).containsEntry(CategoryType.DEFENSE, 77);

        // 모든 이벤트가 발생했는지 확인(5개)
        assertThat(result.occurrences()).hasSize(5);
        assertThat(result.occurrences().stream().map(EventOccurrence::name))
                .contains("태풍 피해", "무역 협정 체결", "대규모 부패 스캔들", "신기술 개발", "군비 경쟁");
    }

    @Test
    void conditions_filter_out_events_and_clamp_scores() {
        Map<CategoryType, Integer> current = scores(10, 10, 10, 10, 10, 99);

        // 조건 불충족 이벤트(ENVIRONMENT < 50)과 과도한 증가 이벤트(ENV + 10) 혼합
        List<EventSpec> specs = List.of(
                ev("조건불만족", "테스트", 1.0, threshold(CategoryType.ENVIRONMENT, LT, 50),
                        Map.of(CategoryType.ECONOMY, +50)
                ),
                ev("상한검증", "테스트", 1.0, threshold(CategoryType.ENVIRONMENT, GE, 90),
                        Map.of(CategoryType.ENVIRONMENT, +10)
                )
        );

        EventEngine engine = new DefaultEventEngine();
        EventResult res = engine.applyEvents(current, specs, () -> 0.0, 1.0);

        // 첫 이벤트는 조건 미충족 → 적용 안 됨, 두 번째는 클램프되어 99+10 -> 100
        assertThat(res.nextScores().get(CategoryType.ECONOMY)).isEqualTo(10);
        assertThat(res.nextScores().get(CategoryType.ENVIRONMENT)).isEqualTo(100);
        assertThat(res.occurrences()).hasSize(1);
    }
}

