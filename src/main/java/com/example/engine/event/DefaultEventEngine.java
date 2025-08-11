package com.example.engine.event;

import com.example.domain.CategoryType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 이벤트 확률/조건을 평가하고 효과를 적용하는 기본 엔진 구현.
 * <p>
 * - 여러 이벤트가 동시 발생할 수 있으며, 카테고리별 델타는 순차적으로 누적됩니다.
 * - 최종 점수는 0~100 범위로 클램프됩니다.
 */
public class DefaultEventEngine implements EventEngine {

    @Override
    public EventResult applyEvents(Map<CategoryType, Integer> currentScores,
                                   List<EventSpec> specs,
                                   EventRandom random,
                                   double frequencyScale) {
        EnumMap<CategoryType, Integer> next = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) {
            next.put(c, currentScores.getOrDefault(c, 50));
        }

        List<EventOccurrence> occurred = new ArrayList<>();

        for (EventSpec spec : specs) {
            if (!spec.condition().evaluate(next)) continue;
            double threshold = spec.probability() * frequencyScale;
            if (random.nextDouble() < threshold) {
                // 효과 적용 및 적용 내역 기록
                EnumMap<CategoryType, Integer> applied = new EnumMap<>(CategoryType.class);
                for (Map.Entry<CategoryType, Integer> e : spec.impact().entrySet()) {
                    CategoryType cat = e.getKey();
                    int delta = e.getValue();
                    int updated = clamp(next.getOrDefault(cat, 50) + delta, 0, 100);
                    next.put(cat, updated);
                    applied.put(cat, delta);
                }
                occurred.add(new EventOccurrence(spec.name(), spec.type(), Map.copyOf(applied)));
            }
        }

        return new EventResult(Map.copyOf(next), List.copyOf(occurred));
    }

    private static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
}
