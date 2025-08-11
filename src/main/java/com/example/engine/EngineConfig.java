package com.example.engine;

import com.example.engine.event.DefaultEventEngine;
import com.example.engine.event.EventEngine;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.engine.event.EventRandom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EngineConfig {

    @Bean
    public IndicatorEngine indicatorEngine() {
        return new BasicIndicatorEngine();
    }

    @Bean
    public EventEngine eventEngine() {
        return new DefaultEventEngine();
    }

    @Bean
    public InteractionMatrixLoader interactionMatrixLoader(ObjectMapper objectMapper) {
        return new InteractionMatrixLoader(objectMapper);
    }

    @Bean
    public EventRandom eventRandom() {
        // 재현 가능한 기본 난수 소스(필요 시 외부 설정으로 시드 주입 고려)
        return EventRandom.fromSeed(42L);
    }
}
