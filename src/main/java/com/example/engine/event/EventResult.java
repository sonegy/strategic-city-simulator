package com.example.engine.event;

import com.example.domain.CategoryType;

import java.util.List;
import java.util.Map;

public record EventResult(
        Map<CategoryType, Integer> nextScores,
        List<EventOccurrence> occurrences
) {}

