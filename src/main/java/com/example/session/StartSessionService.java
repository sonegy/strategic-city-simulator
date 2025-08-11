package com.example.session;

import com.example.domain.*;
import com.example.repository.CategoryScoreRepository;
import com.example.repository.GameSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

@Service
public class StartSessionService {

    private final GameSessionRepository gameSessionRepository;
    private final CategoryScoreRepository categoryScoreRepository;

    public StartSessionService(GameSessionRepository gameSessionRepository,
                               CategoryScoreRepository categoryScoreRepository) {
        this.gameSessionRepository = gameSessionRepository;
        this.categoryScoreRepository = categoryScoreRepository;
    }

    @Transactional
    public Result start(Difficulty difficulty) {
        GameSession session = new GameSession(difficulty, 0, LocalDateTime.now());
        session = gameSessionRepository.save(session);

        EnumMap<CategoryType, Integer> initialScores = new EnumMap<>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) {
            int score = 50; // PRD 기본값 기준 초기 50점
            initialScores.put(c, score);
            categoryScoreRepository.save(new CategoryScore(c, score, session));
        }

        return new Result(session.getId(), difficulty, initialScores);
    }

    public record Result(
            Long sessionId,
            Difficulty difficulty,
            Map<CategoryType, Integer> scores
    ) {}
}

