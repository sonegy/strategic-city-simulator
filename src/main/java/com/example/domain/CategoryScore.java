package com.example.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "category_scores")
public class CategoryScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CategoryType category;

    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private GameSession session;

    protected CategoryScore() {
    }

    public CategoryScore(CategoryType category, Integer score, GameSession session) {
        this.category = category;
        this.score = score;
        this.session = session;
    }

    public Long getId() {
        return id;
    }

    public CategoryType getCategory() {
        return category;
    }

    public Integer getScore() {
        return score;
    }

    public GameSession getSession() {
        return session;
    }
}
