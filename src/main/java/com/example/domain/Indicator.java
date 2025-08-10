package com.example.domain;

import jakarta.persistence.*;

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
