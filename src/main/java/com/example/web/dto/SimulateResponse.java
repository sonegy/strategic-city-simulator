package com.example.web.dto;

import com.example.domain.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

public class SimulateResponse {
    @Schema(description = "시뮬레이션 전 카테고리 점수")
    private Map<CategoryType, Integer> before;
    @Schema(description = "시뮬레이션 후 카테고리 점수")
    private Map<CategoryType, Integer> after;
    @Schema(description = "카테고리별 점수 변화량(after-before)")
    private Map<CategoryType, Integer> delta;
    @Schema(description = "발생 이벤트 목록")
    private List<EventDto> events;
    @Schema(description = "종합 도시 지수(가중 평균)")
    private double overallIndex;

    public SimulateResponse(Map<CategoryType, Integer> before,
                            Map<CategoryType, Integer> after,
                            Map<CategoryType, Integer> delta,
                            List<EventDto> events,
                            double overallIndex) {
        this.before = before;
        this.after = after;
        this.delta = delta;
        this.events = events;
        this.overallIndex = overallIndex;
    }

    public Map<CategoryType, Integer> getBefore() { return before; }
    public Map<CategoryType, Integer> getAfter() { return after; }
    public Map<CategoryType, Integer> getDelta() { return delta; }
    public List<EventDto> getEvents() { return events; }
    public double getOverallIndex() { return overallIndex; }

    public static class EventDto {
        private String name;
        private String type;
        private Map<CategoryType, Integer> impact;

        public EventDto(String name, String type, Map<CategoryType, Integer> impact) {
            this.name = name;
            this.type = type;
            this.impact = impact;
        }

        public String getName() { return name; }
        public String getType() { return type; }
        public Map<CategoryType, Integer> getImpact() { return impact; }
    }
}

