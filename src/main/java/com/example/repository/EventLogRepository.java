package com.example.repository;

import com.example.domain.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventLogRepository extends JpaRepository<EventLog, Long> {
    List<EventLog> findBySessionId(Long sessionId);
    List<EventLog> findBySessionIdAndOccurredAtBetween(Long sessionId, LocalDateTime start, LocalDateTime end);
}
