package com.example.engine;

import com.example.domain.CategoryType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

/**
 * 카테고리 간 상호작용 매트릭스(from→to 가중치)를 클래스패스 리소스에서 로드하는 유틸리티.
 * <p>
 * - 포맷: JSON 오브젝트의 중첩 맵 구조로, 바깥 키는 from(CategoryType), 안쪽 키는 to(CategoryType), 값은 가중치(double).
 * - 예시:
 * <pre>{
 *   "POLITICS": { "ECONOMY": 2.0 },
 *   "ECONOMY": { "ENVIRONMENT": -2.0 }
 * }</pre>
 * - 로드된 결과는 불변 맵으로 변환되어 반환됩니다.
 */
public class InteractionMatrixLoader {

    private final ObjectMapper objectMapper;

    public InteractionMatrixLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 클래스패스 리소스 경로에서 매트릭스를 로드합니다.
     * @param classpathResource 예: "/interaction-matrix.sample.json"
     * @return CategoryType 열거형 키를 사용하는 불변 매트릭스 맵
     * @throws IllegalArgumentException 리소스를 찾을 수 없을 때
     * @throws IllegalStateException 파싱 실패 등 I/O 오류 발생 시
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

    /**
     * 문자열 키 기반의 맵을 CategoryType 열거형 키 기반의 불변 매트릭스로 변환합니다.
     */
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
