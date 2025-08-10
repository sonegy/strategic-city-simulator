package com.example.repository;

import com.example.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RepositoryTests {
    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private IndicatorRepository indicatorRepository;

    @Autowired
    private CategoryScoreRepository categoryScoreRepository;

    @Autowired
    private BudgetAllocationRepository budgetAllocationRepository;

    @Autowired
    private EventLogRepository eventLogRepository;

    @Test
    void saveAndLoadEntities() {
        GameSession session = new GameSession(Difficulty.EASY, 1, LocalDateTime.now());
        session = gameSessionRepository.save(session);

        indicatorRepository.save(new Indicator("Population", 1000, session));
        categoryScoreRepository.save(new CategoryScore(CategoryType.DEFENSE, 50, session));
        budgetAllocationRepository.save(new BudgetAllocation(CategoryType.DEFENSE, 0.2, session));
        eventLogRepository.save(new EventLog("TEST", "test event", LocalDateTime.now(), session));

        assertThat(gameSessionRepository.findById(session.getId())).isPresent();
        assertThat(indicatorRepository.findAll()).hasSize(1);
        assertThat(categoryScoreRepository.findAll()).hasSize(1);
        assertThat(budgetAllocationRepository.findAll()).hasSize(1);
        assertThat(eventLogRepository.findAll()).hasSize(1);
    }
}
