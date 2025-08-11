package com.example.web;

import com.example.domain.Difficulty;
import com.example.session.StartSessionService;
import com.example.web.dto.StartSessionRequest;
import com.example.web.dto.StartSessionResponse;
import com.example.web.dto.SimulateRequest;
import com.example.web.dto.SimulateResponse;
import com.example.engine.usecase.SimulateMonthUseCase;
import com.example.engine.OverallIndexCalculator;
import com.example.domain.CategoryType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/sessions")
@Tag(name = "Sessions")
public class SessionsController {

    private final StartSessionService startSessionService;
    private final SimulateMonthUseCase simulateMonthUseCase;

    public SessionsController(StartSessionService startSessionService,
                              SimulateMonthUseCase simulateMonthUseCase) {
        this.startSessionService = startSessionService;
        this.simulateMonthUseCase = simulateMonthUseCase;
    }

    @Operation(summary = "세션 시작", description = "난이도를 입력 받아 새 게임 세션을 생성하고 초기 카테고리 점수를 반환합니다.")
    @PostMapping
    public ResponseEntity<StartSessionResponse> start(@Validated @RequestBody StartSessionRequest request) {
        Difficulty difficulty = request.getDifficulty();
        var result = startSessionService.start(difficulty);
        var body = new StartSessionResponse(result.sessionId(), result.difficulty(), result.scores());
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
}
