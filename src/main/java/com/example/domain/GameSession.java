package com.example.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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
}
