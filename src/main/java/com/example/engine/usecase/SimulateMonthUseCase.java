package com.example.engine.usecase;

import com.example.domain.CategoryScore;
import com.example.domain.CategoryType;
import com.example.domain.GameSession;
import com.example.engine.IndicatorEngine;
import com.example.engine.IndicatorEngineParams;
import com.example.engine.InteractionMatrixLoader;
import com.example.engine.event.*;
import com.example.repository.CategoryScoreRepository;
import com.example.repository.EventLogRepository;
import com.example.repository.GameSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * [T2-4] 월간 턴 실행 유스케이스.
 * - 입력: 세션 ID, 카테고리별 예산 비율(0.0~1.0)
 * - 처리: 지표 엔진 → 이벤트 엔진 순으로 적용
 * - 저장: 카테고리 점수 업데이트, 이벤트 로그 기록, 세션 월수 증가
 */
@Service
public class SimulateMonthUseCase {

    private final IndicatorEngine indicatorEngine;
    private final EventEngine eventEngine;
    private final InteractionMatrixLoader matrixLoader;
    private final GameSessionRepository sessionRepository;
    private final CategoryScoreRepository scoreRepository;
    private final EventLogRepository eventLogRepository;
    private final EventRandom eventRandom;

    public SimulateMonthUseCase(IndicatorEngine indicatorEngine,
                                EventEngine eventEngine,
                                InteractionMatrixLoader matrixLoader,
                                GameSessionRepository sessionRepository,
                                CategoryScoreRepository scoreRepository,
                                EventLogRepository eventLogRepository,
                                EventRandom eventRandom) {
        this.indicatorEngine = indicatorEngine;
        this.eventEngine = eventEngine;
        this.matrixLoader = matrixLoader;
        this.sessionRepository = sessionRepository;
        this.scoreRepository = scoreRepository;
        this.eventLogRepository = eventLogRepository;
        this.eventRandom = eventRandom;
    }

    @Transactional
    public Result execute(long sessionId, Map<CategoryType, Double> budgetRatios) {
        GameSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NoSuchElementException("세션을 찾을 수 없습니다: " + sessionId));

        // 현재 카테고리 점수 로드 (없다면 50점으로 초기화)
        Map<CategoryType, Integer> currentScores = loadCurrentScores(session);

        // 파라미터 구성(간단한 기본값 + 샘플 매트릭스)
        IndicatorEngineParams params = defaultParams();

        // 1) 지표 업데이트
        Map<CategoryType, Integer> afterIndicators = indicatorEngine.updateCategoryScores(
                currentScores, budgetRatios, params
        );

        // 2) 이벤트 적용
        EventResult eventResult = eventEngine.applyEvents(
                afterIndicators,
                sampleEvents(),
                eventRandom,
                1.0
        );

        // 3) 저장: 점수, 이벤트 로그, 세션 월수 증가
        persistScores(session, eventResult.nextScores());
        persistEvents(session, eventResult.occurrences());
        incrementSessionMonth(session);

        return new Result(eventResult.nextScores(), eventResult.occurrences());
    }

    private Map<CategoryType, Integer> loadCurrentScores(GameSession session) {
        List<CategoryScore> list = scoreRepository.findBySessionId(session.getId());
        Map<CategoryType, Integer> map = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) map.put(c, 50);
        for (CategoryScore cs : list) {
            map.put(cs.getCategory(), cs.getScore());
        }
        return map;
    }

    private void persistScores(GameSession session, Map<CategoryType, Integer> scores) {
        for (Map.Entry<CategoryType, Integer> e : scores.entrySet()) {
            scoreRepository.findBySessionIdAndCategory(session.getId(), e.getKey())
                    .ifPresentOrElse(existing -> {
                        existing.setScore(e.getValue());
                        scoreRepository.save(existing);
                    }, () -> scoreRepository.save(new CategoryScore(e.getKey(), e.getValue(), session)));
        }
    }

    private void persistEvents(GameSession session, List<EventOccurrence> occurrences) {
        LocalDateTime now = LocalDateTime.now();
        occurrences.forEach(ev -> eventLogRepository.save(
                new com.example.domain.EventLog(ev.type(), ev.name(), now, session)
        ));
    }

    private void incrementSessionMonth(GameSession session) {
        session.incrementMonth();
        sessionRepository.save(session);
    }

    private IndicatorEngineParams defaultParams() {
        EnumMap<CategoryType, Double> natural = new EnumMap<>(CategoryType.class);
        EnumMap<CategoryType, Double> eff = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) {
            natural.put(c, 0.0);
            eff.put(c, 1.0);
        }
        Map<CategoryType, Map<CategoryType, Double>> matrix = matrixLoader.loadFromClasspath("/interaction-matrix.sample.json");
        return new IndicatorEngineParams(natural, eff, matrix, 10.0, 1.0);
    }

    private List<EventSpec> sampleEvents() {
        return List.of(
                new EventSpec("태풍 피해", "자연재해", 0.03, Condition.threshold(CategoryType.ENVIRONMENT, ComparisonOperator.LT, 50),
                        Map.of(CategoryType.ECONOMY, -5, CategoryType.ENVIRONMENT, -7)),
                new EventSpec("무역 협정 체결", "외교", 0.04, Condition.threshold(CategoryType.DIPLOMACY, ComparisonOperator.GT, 60),
                        Map.of(CategoryType.ECONOMY, +5, CategoryType.DIPLOMACY, +3)),
                new EventSpec("대규모 부패 스캔들", "정치", 0.02, Condition.threshold(CategoryType.POLITICS, ComparisonOperator.LT, 40),
                        Map.of(CategoryType.POLITICS, -10, CategoryType.DIPLOMACY, -5)),
                new EventSpec("신기술 개발", "기술혁신", 0.01, Condition.threshold(CategoryType.ECONOMY, ComparisonOperator.GT, 70),
                        Map.of(CategoryType.ECONOMY, +8, CategoryType.ENVIRONMENT, +2)),
                new EventSpec("군비 경쟁", "국방", 0.03, Condition.threshold(CategoryType.DEFENSE, ComparisonOperator.GT, 70),
                        Map.of(CategoryType.DEFENSE, +2, CategoryType.ECONOMY, -3, CategoryType.DIPLOMACY, -2))
        );
    }

    public record Result(
            Map<CategoryType, Integer> scores,
            List<EventOccurrence> events
    ) {}
}
