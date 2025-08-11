package com.example.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 게임 진행을 나타내는 세션 엔티티.
 *
 * <p>
 * - difficulty: 난이도(EASY/NORMAL/HARD)
 * - currentMonth: 현재 진행 월(게임 시작 기준 누적 월)
 * - createdAt: 세션 생성 시각
 * </p>
 */
@Entity
@Table(name = "game_sessions")
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private Integer currentMonth;

    private LocalDateTime createdAt;

    protected GameSession() {
    }

    public GameSession(Difficulty difficulty, Integer currentMonth, LocalDateTime createdAt) {
        this.difficulty = difficulty;
        this.currentMonth = currentMonth;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Integer getCurrentMonth() {
        return currentMonth;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCurrentMonth(Integer currentMonth) {
        this.currentMonth = currentMonth;
    }

    public void incrementMonth() {
        int cur = this.currentMonth == null ? 0 : this.currentMonth;
        this.currentMonth = cur + 1;
    }
}
