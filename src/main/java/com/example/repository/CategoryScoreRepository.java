package com.example.repository;

import com.example.domain.CategoryScore;
import com.example.domain.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CategoryScoreRepository extends JpaRepository<CategoryScore, Long> {
    List<CategoryScore> findBySessionId(Long sessionId);
    Optional<CategoryScore> findBySessionIdAndCategory(Long sessionId, CategoryType category);
}
