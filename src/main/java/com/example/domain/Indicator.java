package com.example.domain;

import jakarta.persistence.*;

/**
 * 세션에 속한 개별 지표(LoL-level indicator)를 나타내는 엔티티.
 *
 * <p>
 * - name: 지표 이름(예: "군사력 지수", "GDP")
 * - value: 지표 값(일반적으로 0~100 범위의 점수로 사용, 필요 시 확장 가능)
 * - session: 소속 게임 세션
 * </p>
 */
@Entity
@Table(name = "indicators")
public class Indicator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "metric_value")
    private Integer value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private GameSession session;

    protected Indicator() {
    }

    public Indicator(String name, Integer value, GameSession session) {
        this.name = name;
        this.value = value;
        this.session = session;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public GameSession getSession() {
        return session;
    }
}
