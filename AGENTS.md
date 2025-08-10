# Repository Guidelines

## Project Structure & Modules
- `src/main/java`: Application source (Spring Boot entrypoint: `com.example.StrategicCitySimulatorApplication`).
- `src/test/java`: Tests (JUnit 5). Mirror packages; suffix classes with `Tests`.
- `build.gradle` / `settings.gradle`: Gradle configuration (Java 21, Spring Boot 3.2).
- `.github/ISSUE_TEMPLATE`: Issue templates for task/feature tracking.
- `build/`: Generated artifacts. Do not edit.

## Build, Test, and Run
- Build: `./gradlew build` — compiles, runs tests, and creates artifacts.
- Test: `./gradlew test` — runs unit/integration tests (JUnit Platform).
- Clean: `./gradlew clean` — removes build outputs.
- Run locally: `./gradlew bootRun` — starts the Spring Boot app.

## Coding Style & Naming
- Language: Java 21, Spring Boot 3.2.
- Indentation: 4 spaces; UTF-8 source files.
- Naming: packages `lowercase`, classes `PascalCase`, methods/fields `camelCase`, constants `UPPER_SNAKE_CASE`.
- Imports/formatting: Use IDE formatter and “Optimize Imports”. Keep lines ≤ 120 chars when reasonable.
- Nullability: Prefer constructor injection; avoid `null` where possible; use `Optional` for return types when appropriate.

## Testing Guidelines
- Framework: JUnit 5 with Spring Boot testing support.
- Location: `src/test/java/<same package>`.
- Naming: `*Tests` (e.g., `StrategicCitySimulatorApplicationTests`).
- Scope: Fast, deterministic tests. Favor unit tests; use `@SpringBootTest` only when context is needed.
- Run: `./gradlew test` (CI runs the same).

## Commit & Pull Requests
- Commit style: Imperative, concise messages. Prefer conventional prefixes: `feat:`, `fix:`, `chore:`, `docs:`, `test:`, `refactor:` (e.g., `chore: add Gradle Spring Boot bootstrap`).
- Reference issues: `Fixes #123` or `Refs #123`.
- Pull requests: Small, focused changes with a clear description, screenshots/logs when UI or behavior changes, and reproduction/verification steps. Link related issues and note breaking changes.

## Security & Configuration
- Configuration: Place app config in `src/main/resources/application.yml` or `.properties`. Do not commit secrets; use environment variables or externalized config.
- Sensitive files: Keep secrets out of VCS; `.gitignore` already excludes common build outputs.

## Architecture Notes
- Entry point: `StrategicCitySimulatorApplication`. Start new features under `com.example.<feature>` packages. Keep domain, service, and web layers separated as the project grows.

