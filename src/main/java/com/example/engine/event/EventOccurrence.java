package com.example.engine.event;

import com.example.domain.CategoryType;

import java.util.Map;

/**
 * 한 턴 동안 실제로 발생한 이벤트의 스냅샷.
 * - name/type: 이벤트 정보
 * - appliedImpact: 적용된 카테고리별 델타(클램프 이전의 원 델타 값)
 */
public record EventOccurrence(
        String name,
        String type,
        Map<CategoryType, Integer> appliedImpact
) {}
