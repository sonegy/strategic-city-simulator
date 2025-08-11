package com.example.engine.usecase;

import com.example.domain.*;
import com.example.repository.CategoryScoreRepository;
import com.example.repository.GameSessionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SimulateMonthUseCaseTests {

    @Autowired
    private SimulateMonthUseCase useCase;

    @Autowired
    private GameSessionRepository sessionRepository;

    @Autowired
    private CategoryScoreRepository scoreRepository;

    @Test
    void simulate_one_month_and_persist_in_h2() {
        GameSession session = sessionRepository.save(new GameSession(Difficulty.NORMAL, 0, LocalDateTime.now()));
        for (CategoryType c : CategoryType.values()) {
            scoreRepository.save(new CategoryScore(c, 50, session));
        }

        EnumMap<CategoryType, Double> ratios = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) ratios.put(c, 0.0);
        ratios.put(CategoryType.ECONOMY, 0.5);
        ratios.put(CategoryType.POLITICS, 0.2);

        SimulateMonthUseCase.Result result = useCase.execute(session.getId(), ratios);

        // 결과 점수는 모든 카테고리에 대해 0~100 범위
        assertThat(result.scores().keySet()).containsExactlyInAnyOrder(CategoryType.values());
        result.scores().values().forEach(v -> assertThat(v).isBetween(0, 100));

        // 새 점수 6개가 저장되었는지(단순 추가 방식)
        long count = scoreRepository.findAll().stream()
                .filter(s -> s.getSession().getId().equals(session.getId()))
                .count();
        assertThat(count).isEqualTo(6); // 업데이트 방식으로 유지

        // 세션 월수 +1 증가 확인
        assertThat(sessionRepository.findById(session.getId()).orElseThrow().getCurrentMonth())
                .isEqualTo(1);
    }
}
