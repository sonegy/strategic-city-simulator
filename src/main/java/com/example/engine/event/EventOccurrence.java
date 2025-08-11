package com.example.engine.event;

import com.example.domain.CategoryType;

import java.util.Map;

public record EventOccurrence(
        String name,
        String type,
        Map<CategoryType, Integer> appliedImpact
) {}

