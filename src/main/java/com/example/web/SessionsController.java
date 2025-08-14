package com.example.web;

import com.example.domain.Difficulty;
import com.example.session.StartSessionService;
import com.example.web.dto.StartSessionRequest;
import com.example.web.dto.StartSessionResponse;
import com.example.web.dto.SimulateRequest;
import com.example.web.dto.SimulateResponse;
import com.example.web.dto.ReportResponse;
import com.example.engine.usecase.SimulateMonthUseCase;
import com.example.engine.OverallIndexCalculator;
import com.example.domain.CategoryType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/sessions")
@Tag(name = "Sessions")
public class SessionsController {

    private final StartSessionService startSessionService;
    private final SimulateMonthUseCase simulateMonthUseCase;
    private final com.example.repository.CategoryScoreRepository scoreRepository;
    private final com.example.repository.EventLogRepository eventLogRepository;

    public SessionsController(StartSessionService startSessionService,
                              SimulateMonthUseCase simulateMonthUseCase,
                              com.example.repository.CategoryScoreRepository scoreRepository,
                              com.example.repository.EventLogRepository eventLogRepository) {
        this.startSessionService = startSessionService;
        this.simulateMonthUseCase = simulateMonthUseCase;
        this.scoreRepository = scoreRepository;
        this.eventLogRepository = eventLogRepository;
    }

    @Operation(summary = "세션 시작", description = "난이도를 입력 받아 새 게임 세션을 생성하고 초기 카테고리 점수를 반환합니다.")
    @PostMapping
    public ResponseEntity<StartSessionResponse> start(@Validated @RequestBody StartSessionRequest request) {
        Difficulty difficulty = request.getDifficulty();
        var result = startSessionService.start(difficulty);
        var body = new StartSessionResponse(
                result.sessionId(),
                result.difficulty(),
                result.scores(),
                result.initialBudget(),
                result.treasury()
        );
        return ResponseEntity.created(URI.create("/api/v1/sessions/" + result.sessionId()))
                .body(body);
    }

    @Operation(summary = "월간 시뮬레이션 실행", description = "예산 배분 비율을 입력받아 한 달 시뮬레이션을 실행하고 결과를 반환합니다.")
    @PostMapping("/{id}/simulate")
    public ResponseEntity<SimulateResponse> simulate(@PathVariable("id") long sessionId,
                                                     @Validated @RequestBody SimulateRequest request) {
        var res = simulateMonthUseCase.execute(sessionId, request.getBudgetRatios());

        // 델타 계산
        var delta = new java.util.EnumMap<CategoryType, Integer>(CategoryType.class);
        for (CategoryType c : CategoryType.values()) {
            int before = res.before().getOrDefault(c, 50);
            int after = res.scores().getOrDefault(c, before);
            delta.put(c, after - before);
        }

        // 이벤트 DTO 변환
        var events = res.events().stream()
                .map(e -> new SimulateResponse.EventDto(e.name(), e.type(), e.appliedImpact()))
                .toList();

        double overall = OverallIndexCalculator.compute(res.scores());
        var body = new SimulateResponse(res.before(), res.scores(), delta, events, overall);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "리포트 조회", description = "월간 요약과 누적 통계를 반환합니다. year, month가 없으면 최신 스냅샷 기준.")
    @GetMapping("/{id}/reports")
    public ResponseEntity<ReportResponse> report(@PathVariable("id") long sessionId,
                                                 @RequestParam(value = "year", required = false) Integer year,
                                                 @RequestParam(value = "month", required = false) Integer month) {
        var scores = scoreRepository.findBySessionId(sessionId).stream()
                .collect(Collectors.toMap(
                        com.example.domain.CategoryScore::getCategory,
                        com.example.domain.CategoryScore::getScore
                ));
        double overall = OverallIndexCalculator.compute(scores);

        LocalDateTime start = null, end = null;
        if (year != null && month != null) {
            LocalDate first = LocalDate.of(year, month, 1);
            start = first.atStartOfDay();
            end = first.withDayOfMonth(first.lengthOfMonth()).atTime(LocalTime.MAX);
        }

        var events = (start != null)
                ? eventLogRepository.findBySessionIdAndOccurredAtBetween(sessionId, start, end)
                : eventLogRepository.findBySessionId(sessionId);

        var eventDtos = events.stream()
                .map(e -> new ReportResponse.ReportEventDto(e.getType(), e.getDescription(), e.getOccurredAt()))
                .toList();

        var cumulativeByType = eventLogRepository.findBySessionId(sessionId).stream()
                .collect(Collectors.groupingBy(com.example.domain.EventLog::getType, Collectors.counting()));

        return ResponseEntity.ok(new ReportResponse(year, month, scores, overall, eventDtos, cumulativeByType));
    }
}
