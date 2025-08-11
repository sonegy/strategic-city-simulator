package com.example.engine;

import com.example.domain.CategoryType;

import java.util.Map;

/**
 * PRD 기준 종합 도시 지수 계산기.
 * 가중치: 경제25, 정치20, 국방15, 외교15, 문화15, 환경10 (합 100)
 */
public final class OverallIndexCalculator {
    private OverallIndexCalculator() {}

    public static double compute(Map<CategoryType, Integer> scores) {
        double econ = scores.getOrDefault(CategoryType.ECONOMY, 50);
        double pol = scores.getOrDefault(CategoryType.POLITICS, 50);
        double def = scores.getOrDefault(CategoryType.DEFENSE, 50);
        double dip = scores.getOrDefault(CategoryType.DIPLOMACY, 50);
        double cul = scores.getOrDefault(CategoryType.CULTURE, 50);
        double env = scores.getOrDefault(CategoryType.ENVIRONMENT, 50);
        double weighted = econ*25 + pol*20 + def*15 + dip*15 + cul*15 + env*10;
        return weighted / 100.0;
    }
}

