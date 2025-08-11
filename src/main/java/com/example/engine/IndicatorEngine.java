package com.example.engine;

import com.example.domain.CategoryType;
import java.util.Map;

/**
 * 지표(카테고리 점수) 업데이트를 수행하는 엔진 인터페이스.
 */
public interface IndicatorEngine {
    /**
     * 현재 카테고리 점수와 예산 배분, 파라미터를 입력받아 다음 턴 카테고리 점수를 계산한다.
     *
     * @param currentScores  카테고리별 현재 점수(0~100 권장)
     * @param budgetRatios   카테고리별 예산 비율(0.0~1.0 가정, 합 1.0 비필수)
     * @param params         엔진 파라미터(자연감소/투자효율/상호작용/스케일)
     * @return 카테고리별 업데이트된 점수(0~100로 클램프)
     */
    Map<CategoryType, Integer> updateCategoryScores(
            Map<CategoryType, Integer> currentScores,
            Map<CategoryType, Double> budgetRatios,
            IndicatorEngineParams params
    );
}

