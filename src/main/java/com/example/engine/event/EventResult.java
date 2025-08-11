package com.example.engine.event;

import com.example.domain.CategoryType;

import java.util.List;
import java.util.Map;

/**
 * 이벤트 처리 결과.
 * - nextScores: 이벤트 적용 후의 다음 턴 카테고리 점수
 * - occurrences: 해당 턴 동안 실제 발생한 이벤트 목록
 */
public record EventResult(
        Map<CategoryType, Integer> nextScores,
        List<EventOccurrence> occurrences
) {}
