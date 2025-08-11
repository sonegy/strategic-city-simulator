package com.example.engine.event;

import com.example.domain.CategoryType;

import java.util.List;
import java.util.Map;

/**
 * 이벤트 확률/조건/효과를 적용하여 다음 턴의 카테고리 점수와 발생 이벤트 목록을 산출하는 엔진.
 */
public interface EventEngine {
    /**
     * 주어진 현재 점수와 이벤트 정의 리스트를 평가하여 결과를 반환합니다.
     * @param currentScores 현재 카테고리 점수
     * @param specs 평가할 이벤트 정의 목록
     * @param random 확률 판정을 위한 난수 소스(재현성 보장 가능)
     * @param frequencyScale 전역 이벤트 빈도 스케일(난이도 등으로 조정)
     */
    EventResult applyEvents(
            Map<CategoryType, Integer> currentScores,
            List<EventSpec> specs,
            EventRandom random,
            double frequencyScale
    );
}
