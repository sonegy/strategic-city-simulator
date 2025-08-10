package com.example.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "budget_allocations")
public class BudgetAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CategoryType category;

    private Double ratio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private GameSession session;

    protected BudgetAllocation() {
    }

    public BudgetAllocation(CategoryType category, Double ratio, GameSession session) {
        this.category = category;
        this.ratio = ratio;
        this.session = session;
    }

    public Long getId() {
        return id;
    }

    public CategoryType getCategory() {
        return category;
    }

    public Double getRatio() {
        return ratio;
    }

    public GameSession getSession() {
        return session;
    }
}
