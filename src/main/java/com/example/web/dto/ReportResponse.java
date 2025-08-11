package com.example.web.dto;

import com.example.domain.CategoryType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ReportResponse {
    private Integer year;
    private Integer month;
    private Map<CategoryType, Integer> scores;
    private double overallIndex;
    private List<ReportEventDto> events;
    private Map<String, Long> cumulativeEventsByType;

    public ReportResponse(Integer year, Integer month,
                          Map<CategoryType, Integer> scores,
                          double overallIndex,
                          List<ReportEventDto> events,
                          Map<String, Long> cumulativeEventsByType) {
        this.year = year;
        this.month = month;
        this.scores = scores;
        this.overallIndex = overallIndex;
        this.events = events;
        this.cumulativeEventsByType = cumulativeEventsByType;
    }

    public Integer getYear() { return year; }
    public Integer getMonth() { return month; }
    public Map<CategoryType, Integer> getScores() { return scores; }
    public double getOverallIndex() { return overallIndex; }
    public List<ReportEventDto> getEvents() { return events; }
    public Map<String, Long> getCumulativeEventsByType() { return cumulativeEventsByType; }

    public static class ReportEventDto {
        private String type;
        private String description;
        private LocalDateTime occurredAt;

        public ReportEventDto(String type, String description, LocalDateTime occurredAt) {
            this.type = type;
            this.description = description;
            this.occurredAt = occurredAt;
        }

        public String getType() { return type; }
        public String getDescription() { return description; }
        public LocalDateTime getOccurredAt() { return occurredAt; }
    }
}

