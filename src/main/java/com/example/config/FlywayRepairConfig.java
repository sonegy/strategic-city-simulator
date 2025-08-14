package com.example.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 로컬/TC/개발 프로필에서만 Flyway repair를 선행하여
 * 체크섬 변경(개발 중 마이그레이션 수정)으로 인한 검증 실패를 자동 치유한다.
 * 운영(prod)에는 적용하지 않는다.
 */
@Configuration
@Profile({"local", "tc", "dev"})
public class FlywayRepairConfig {

    @Bean
    public FlywayMigrationStrategy flywayRepairThenMigrate() {
        return (Flyway flyway) -> {
            // 기존 이력의 checksum을 현재 스크립트로 정정 후 마이그레이션 수행
            flyway.repair();
            flyway.migrate();
        };
    }
}

