package com.example.web;

import com.example.domain.Difficulty;
import com.example.session.StartSessionService;
import com.example.web.dto.StartSessionRequest;
import com.example.web.dto.StartSessionResponse;
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

    public SessionsController(StartSessionService startSessionService) {
        this.startSessionService = startSessionService;
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
}

