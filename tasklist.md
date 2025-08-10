

---

### [T0-1] Gradle/SB3 템플릿 부트스트랩
**내용**
- JDK 21, Spring Boot 3.x(LTS), Gradle Kotlin DSL 프로젝트 생성. 기본 패키지 `com.citysim.app`.

**산출물**
- `build.gradle.kts`, `settings.gradle.kts`, `Application.kt`/`Application.java`.

**DOD**
- [ ] 앱 로컬 부팅(8080) 및 `/actuator/health` 200 OK.

---

### [T0-2] 기본 의존성 구성
**내용**
- Spring Web, Validation, Spring Data JPA, PostgreSQL Driver, Actuator, Testcontainers.

**산출물**
- 의존성 목록 업데이트, `application-local.yaml`.

**DOD**
- [ ] 빈 컨텍스트 로드 테스트 통과.

---

### [T0-3] Docker 빌드 파이프라인(로컬)
**내용**
- 경량 `Dockerfile`(distroless/temurin), multi-stage build.

**산출물**
- `Dockerfile`, `docker-compose.yml`(local pg 포함 선택).

**DOD**
- [ ] `docker run`으로 앱 기동 및 `/actuator/health` 확인.

---

### [T0-4] GitHub Actions CI (단위테스트+도커빌드)
**내용**
- PR 시 테스트/빌드, main 머지 시 이미지 빌드&푸시(Artifact Registry는 T6에서).

**산출물**
- `.github/workflows/ci.yml`.

**DOD**
- [ ] 테스트 통과
- [ ] 이미지 로컬 레지스트리 푸시 시뮬레이션

---

### [T1-1] ERD 초안 및 JPA 엔티티 스켈레톤
**내용**
- `Indicator`, `CategoryScore`, `BudgetAllocation`, `EventLog`, `GameSession` 기본 엔티티/리포지토리.

**산출물**
- ERD png/md, 엔티티 클래스, 리포지토리 인터페이스.

**DOD**
- [ ] 스키마 자동 생성
- [ ] 기본 CRUD 테스트 통과

---

### [T1-2] 마이그레이션 도구 도입(Flyway)
**내용**
- Flyway 베이스라인 및 V1__init.sql 작성.

**산출물**
- `/resources/db/migration/V1__init.sql`.

**DOD**
- [ ] 앱 기동 시 마이그레이션 성공 로그

---

### [T1-3] 구성 분리
**내용**
- `application-{local,dev,prod}.yaml`, 프로필별 DB/로깅/보안.

**DOD**
- [ ] 로컬/프로드 프로필 스위칭 테스트

---

### [T2-1] 지표 업데이트 포뮬러 모듈
**내용**
- `IndicatorEngine` 인터페이스 + 기본 구현(투자효율/자연변화/상호작용 파라미터 주입).

**산출물**
- 서비스/유닛 테스트.

**DOD**
- [ ] 단위 테스트로 공식 검증(예상값 ±오차)

---

### [T2-2] 상호작용 매트릭스 모듈
**내용**
- 카테고리 간 영향도 행렬 로딩( YAML/JSON ).

**DOD**
- [ ] 샘플 매트릭스 로딩 및 적용 테스트

---

### [T2-3] 이벤트 처리기(확률/조건/효과)
**내용**
- `EventEngine`—확률 계산, 조건 평가, 효과 적용.

**DOD**
- [ ] 5개 샘플 이벤트에 대한 시뮬레이션 테스트

---

### [T2-4] 월간 턴 실행 유스케이스
**내용**
- `SimulateMonthUseCase(sessionId, allocations)`; 엔진 호출→결과 저장.

**DOD**
- [ ] 인메모리/pg 환경에서 월 턴 진행 통합테스트

---

### [T3-1] 세션 시작 API
**내용**
- `POST /api/v1/sessions` (난이도/프리셋).

**DOD**
- [ ] 201 Created & 초기 지표 반환
- [ ] OpenAPI 명세 포함

---

### [T3-2] 예산 배분/시뮬레이트 API
**내용**
- `POST /api/v1/sessions/{id}/simulate` (월 예산 배분 입력).

**DOD**
- [ ] 결과에 지표 변화/이벤트/카테고리/종합지수 포함

---

### [T3-3] 리포트 조회 API
**내용**
- `GET /api/v1/sessions/{id}/reports?year=&month=`.

**DOD**
- [ ] 월간 요약/누적 통계 반환

---

### [T3-4] OpenAPI 문서화 & 스니펫 테스트
**내용**
- springdoc-openapi UI, REST Docs 또는 springdoc Tests.

**DOD**
- [ ] `/swagger-ui` 노출
- [ ] 기본 시나리오 스니펫 생성

---

### [T4-1] 미니 대시보드(정적/SPA 한 페이지)
**내용**
- Vite + React 최소 템플릿, 예산 슬라이더, 결과 테이블.

**DOD**
- [ ] 로컬에서 백엔드 연동
- [ ] 한 턴 시뮬레이트 가능

---

### [T4-2] 월간 보고서 표/간단 차트
**내용**
- 변화 화살표/미니 차트(Chart.js).

**DOD**
- [ ] 3개 지표 이상 그래프 표시

---

### [T5-1] 단위/통합 테스트 기본 세트
**내용**
- JUnit5/AssertJ/MockMvc, Testcontainers(Postgres).

**DOD**
- [ ] 라인 커버리지 60%+
- [ ] 핵심 유스케이스 통합 테스트

---

### [T5-2] 시뮬레이션 회귀 테스트 스냅샷
**내용**
- 고정 시드로 12개월 실행, 결과 스냅샷 비교.

**DOD**
- [ ] 변경 시 결과 차이 탐지 알림

---

### [T5-3] 부하 테스트(가벼운)
**내용**
- k6 or JMeter로 간단 RPS, 95p 지연 < 200ms(시뮬레이트).

**DOD**
- [ ] 리포트 아카이브

---

### [T6-1] GCP 프로젝트/레지스트리/권한
**내용**
- Artifact Registry 생성, 서비스 계정/워크로드 아이덴티티.

**DOD**
- [ ] `gcloud`로 로그인 없이 GA에서 푸시 성공

---

### [T6-2] Cloud SQL(Postgres) 준비
**내용**
- 인스턴스/DB/사용자 생성, VPC/프록시 방식 결정(Cloud Run 연결).

**DOD**
- [ ] 로컬→Cloud SQL Proxy로 연결 테스트

---

### [T6-3] Secret Manager 통합
**내용**
- DB 비번, JWT 시크릿 등 저장; SB에서 Secret Manager 연동.

**DOD**
- [ ] 프로퍼티 주입 성공 및 로깅 마스킹

---

### [T6-4] Cloud Run 배포 파이프라인
**내용**
- GA CD 워크플로우(main 머지 시 배포), 리비전 자동 롤백 전략.

**DOD**
- [ ] `/actuator/health` 정상
- [ ] 트래픽 스플릿 배포 확인

---

### [T6-5] 로깅/모니터링
**내용**
- Cloud Logging/Cloud Monitoring 연동, 구조화 로그(JSON).

**DOD**
- [ ] 대시보드 1개
- [ ] 경보(에러율/레이터시) 2개

---

### [T7-1] 구성 유효성 검증
**내용**
- `@ConfigurationProperties` + validation.

**DOD**
- [ ] 잘못된 설정으로 기동 실패 시 명확한 에러

---

### [T7-2] 프로파일별 외부화 구성
**내용**
- prod는 Secret Manager, dev/local은 `.env`/yaml.

**DOD**
- [ ] 세 가지 프로필 모두 기동 OK

---

### [T7-3] 감사/이벤트 로그
**내용**
- 주요 액션 감사 로그, 이벤트 결과 `EventLog` 저장.

**DOD**
- [ ] 월 턴 실행 시 로그/DB 기록 확인

---

### [T8-1] 운영/개발 README
**내용**
- 로컬 실행, 테스트, 도커 빌드, GCP 배포 가이드.

**DOD**
- [ ] 신입이 따라 해서 30분 내 로컬 실행 성공

---

### [T8-2] API 사용 가이드
**내용**
- cURL/HTTPie 샘플, 응답 예시, 에러 코드 표.

**DOD**
- [ ] 최소 5개 시나리오 샘플 포함

---

### [T8-3] 설정/비밀키 관리 정책
**내용**
- 환경변수/시크릿, 회전 절차.

**DOD**
- [ ] 문서 승인

---

### [T9-1] 하이브리드 저장(스냅샷 압축/리플레이)
---
### [T9-2] 난이도 프리셋 편집 UI
---
### [T9-3] AI 정책 어드바이저(권고 예산 비율)
---
### [T9-4] 이벤트 모더레이션 파일 외부화(Admin UI)
---
### [T9-5] 멀티 세션 동시 실행 최적화

---
