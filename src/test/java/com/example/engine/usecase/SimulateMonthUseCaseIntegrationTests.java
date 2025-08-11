package com.example.engine.usecase;

import com.example.domain.*;
import com.example.repository.CategoryScoreRepository;
import com.example.repository.GameSessionRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ActiveProfiles("tc")
@Tag("integration")
class SimulateMonthUseCaseIntegrationTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    private final SimulateMonthUseCase useCase;
    private final GameSessionRepository sessionRepository;
    private final CategoryScoreRepository scoreRepository;

    @Autowired
    SimulateMonthUseCaseIntegrationTests(SimulateMonthUseCase useCase,
                                         GameSessionRepository sessionRepository,
                                         CategoryScoreRepository scoreRepository) {
        this.useCase = useCase;
        this.sessionRepository = sessionRepository;
        this.scoreRepository = scoreRepository;
    }

    @Test
    void simulate_one_month_in_postgres() {
        GameSession session = sessionRepository.save(new GameSession(Difficulty.NORMAL, 0, LocalDateTime.now()));
        for (CategoryType c : CategoryType.values()) {
            scoreRepository.save(new CategoryScore(c, 50, session));
        }

        EnumMap<CategoryType, Double> ratios = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) ratios.put(c, 0.0);
        ratios.put(CategoryType.ECONOMY, 0.5);
        ratios.put(CategoryType.POLITICS, 0.2);

        SimulateMonthUseCase.Result result = useCase.execute(session.getId(), ratios);

        assertThat(result.scores().keySet()).containsExactlyInAnyOrder(CategoryType.values());
    }
}
