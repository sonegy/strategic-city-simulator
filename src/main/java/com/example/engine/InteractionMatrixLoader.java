package com.example.engine;

import com.example.domain.CategoryType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

/**
 * 상호작용 매트릭스를 JSON(또는 호환 포맷)으로부터 로딩하는 유틸리티.
 * 기대 포맷 예시:
 * {
 *   "POLITICS": { "ECONOMY": 2.0 },
 *   "ECONOMY": { "ENVIRONMENT": -2.0 }
 * }
 */
public class InteractionMatrixLoader {

    private final ObjectMapper objectMapper;

    public InteractionMatrixLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 클래스패스 리소스 경로에서 매트릭스를 로드한다.
     * @param classpathResource 예: "/interaction-matrix.sample.json"
     */
    public Map<CategoryType, Map<CategoryType, Double>> loadFromClasspath(String classpathResource) {
        try (InputStream is = getResourceAsStream(classpathResource)) {
            if (is == null) {
                throw new IllegalArgumentException("리소스를 찾을 수 없습니다: " + classpathResource);
            }
            Map<String, Map<String, Double>> raw = objectMapper.readValue(
                    is,
                    new TypeReference<>() {}
            );
            return toEnumMatrix(raw);
        } catch (IOException e) {
            throw new IllegalStateException("상호작용 매트릭스 로딩 실패: " + classpathResource, e);
        }
    }

    private static InputStream getResourceAsStream(String path) {
        if (path == null) return null;
        // 절대/상대 모두 지원: 앞에 '/'가 없으면 붙여서 시도
        InputStream is = InteractionMatrixLoader.class.getResourceAsStream(path);
        if (is == null && !path.startsWith("/")) {
            is = InteractionMatrixLoader.class.getResourceAsStream("/" + path);
        }
        return is;
    }

    private static Map<CategoryType, Map<CategoryType, Double>> toEnumMatrix(
            Map<String, Map<String, Double>> raw
    ) {
        EnumMap<CategoryType, Map<CategoryType, Double>> outer = new EnumMap<>(CategoryType.class);
        if (raw == null) return Map.copyOf(outer);
        raw.forEach((fromKey, innerMap) -> {
            CategoryType from = CategoryType.valueOf(fromKey);
            EnumMap<CategoryType, Double> inner = new EnumMap<>(CategoryType.class);
            if (innerMap != null) {
                innerMap.forEach((toKey, v) -> inner.put(CategoryType.valueOf(toKey), v));
            }
            outer.put(from, Map.copyOf(inner));
        });
        return Map.copyOf(outer);
    }
}

