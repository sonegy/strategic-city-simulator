package com.example.engine.event;

import com.example.domain.CategoryType;

import java.util.List;
import java.util.Map;

public interface EventEngine {
    EventResult applyEvents(
            Map<CategoryType, Integer> currentScores,
            List<EventSpec> specs,
            EventRandom random,
            double frequencyScale
    );
}

