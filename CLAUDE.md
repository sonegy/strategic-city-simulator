# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Strategic City Simulator is a Korean city-building simulation game designed for easy gameplay and quick feedback. The system uses a Spring Boot 3.x backend with PostgreSQL for data persistence and React frontend for the user interface.

### Target Architecture
- **Backend**: Java/Kotlin with Spring Boot 3.x, Gradle Kotlin DSL
- **Database**: PostgreSQL with Flyway migrations  
- **Frontend**: React with Vite build system
- **Deployment**: Google Cloud Platform (Cloud Run, Cloud SQL, Artifact Registry)
- **Package Structure**: `com.citysim.app` as base package

## Development Commands

### Local Development Setup
```bash
# Start with Docker Compose (includes PostgreSQL)
docker-compose up -d

# Run application locally
./gradlew bootRun --args='--spring.profiles.active=local'

# Run tests
./gradlew test

# Run integration tests with Testcontainers
./gradlew integrationTest

# Build Docker image
./gradlew bootBuildImage
```

### Database Operations
```bash
# Run Flyway migrations manually
./gradlew flywayMigrate

# Generate JPA entities from existing schema
./gradlew generateEntities
```

### Testing Commands
```bash
# Unit tests only
./gradlew test --tests "*Unit*"

# Integration tests with database
./gradlew test --tests "*Integration*"

# Load testing with k6
k6 run src/test/k6/load-test.js
```

## Core Architecture

### Game Logic Flow
1. **Session Management**: `GameSession` entity tracks game state and progress
2. **Budget Allocation**: Monthly budget distributed across 6 categories (Defense, Diplomacy, Economy, Politics, Culture, Environment)
3. **Simulation Engine**: `IndicatorEngine` processes investment effects, natural decay, and cross-category interactions
4. **Event System**: `EventEngine` handles random events based on current city state
5. **Reporting**: Monthly reports generated showing indicator changes and city index

### Key Domain Models

#### Core Entities
- `GameSession`: Tracks overall game state, difficulty, and current month/year
- `Indicator`: Low-level metrics (GDP, unemployment, military strength, etc.)
- `CategoryScore`: Mid-level aggregated scores for each category
- `BudgetAllocation`: Monthly budget distribution records
- `EventLog`: Records of events that occurred and their impacts

#### Service Layer Architecture
- `SimulateMonthUseCase`: Orchestrates monthly turn processing
- `IndicatorEngine`: Handles indicator calculations and interactions
- `EventEngine`: Manages event probability, conditions, and effects
- `InteractionMatrix`: Manages cross-category influence effects

### Database Schema Design
- **indicator_values**: Time-series data for all indicators
- **budget_allocations**: Historical budget allocation records  
- **events**: Event occurrence log with JSON impact data
- **game_sessions**: Game state management

### API Design Patterns
- RESTful API following `/api/v1/sessions/{id}/...` pattern
- OpenAPI 3.0 documentation with springdoc-openapi
- Standardized error responses with problem details
- Request/response DTOs separate from domain entities

## Configuration Management

### Profile-Based Configuration
- **local**: Uses embedded H2 or local PostgreSQL, debug logging
- **dev**: Uses Cloud SQL Proxy, structured JSON logging
- **prod**: Uses Cloud SQL direct connection, Secret Manager integration

### Environment Variables
```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=citysim
DB_USER=citysim_user

# Google Cloud (prod only)
GOOGLE_PROJECT_ID=your-project
GOOGLE_CLOUD_SQL_INSTANCE=your-instance
```

## Testing Strategy

### Unit Testing
- JUnit 5 with AssertJ for assertions
- Mockito for service layer mocking
- Target 70%+ line coverage for core business logic

### Integration Testing  
- Testcontainers for database integration tests
- MockMvc for API endpoint testing
- Full simulation scenarios with known expected outcomes

### Performance Testing
- k6 scripts for API load testing
- Target: 95th percentile response time < 200ms for simulation endpoints
- Memory usage monitoring for long-running simulations

## Game Balance Configuration

### Indicator Parameters
Located in `src/main/resources/config/indicators.yaml`:
- Investment efficiency coefficients
- Natural decay rates  
- Min/max value constraints
- Cross-category interaction matrix

### Event Configuration  
Located in `src/main/resources/config/events.yaml`:
- Event probability tables
- Trigger conditions
- Impact formulas

### Difficulty Settings
Located in `src/main/resources/config/difficulty.yaml`:
- Initial budget amounts
- Investment efficiency multipliers
- Event frequency modifiers

## Deployment and Operations

### Google Cloud Deployment
```bash
# Deploy to Cloud Run
gcloud run deploy citysim-api \
  --source . \
  --platform managed \
  --region asia-northeast3

# View logs
gcloud logs read --project=your-project --service=citysim-api
```

### Monitoring and Observability
- Cloud Logging with structured JSON format
- Cloud Monitoring dashboards for key metrics
- Application-level health checks at `/actuator/health`
- Custom metrics for simulation performance

## Development Guidelines

### Code Organization
- Domain-driven design with clear bounded contexts
- Separate packages for `domain`, `application`, `infrastructure`
- Configuration classes in `config` package
- API controllers in `api` package

### Data Migration Strategy
- Use Flyway for all schema changes
- Backwards-compatible migrations only
- Seed data for initial game parameters
- Version control all migration scripts

### Security Considerations
- No authentication required (single-player game)
- Input validation on all API endpoints
- SQL injection prevention with JPA/JPQL
- Secret management via Google Secret Manager in production