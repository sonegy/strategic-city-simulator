package com.example.repository;

import com.example.domain.Indicator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndicatorRepository extends JpaRepository<Indicator, Long> {
}
