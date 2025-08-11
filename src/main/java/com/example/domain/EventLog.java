package com.example.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 시뮬레이션 중 발생한 이벤트 기록을 저장하는 엔티티.
 *
 * <p>
 * - type: 이벤트 유형(예: 자연재해/외교/경제 등)
 * - description: 이벤트 설명
 * - occurredAt: 발생 시각
 * - session: 소속 게임 세션
 * </p>
 */
@Entity
@Table(name = "event_logs")
public class EventLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String description;

    private LocalDateTime occurredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private GameSession session;

    protected EventLog() {
    }

    public EventLog(String type, String description, LocalDateTime occurredAt, GameSession session) {
        this.type = type;
        this.description = description;
        this.occurredAt = occurredAt;
        this.session = session;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public GameSession getSession() {
        return session;
    }
}
