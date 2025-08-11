package com.example.domain;

import jakarta.persistence.*;

/**
 * 카테고리(국방/외교/경제/정치/문화/환경) 단위의 점수를 저장하는 엔티티.
 *
 * <p>
 * - category: 점수의 대상 카테고리
 * - score: 0~100 범위의 점수(권장)
 * - session: 소속 게임 세션
 * </p>
 */
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
