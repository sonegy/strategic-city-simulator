# Docker 로컬 빌드 파이프라인

이 문서는 Strategic City Simulator(Spring Boot 3.2, Java 21)를 Docker로 로컬에서 빌드/실행하는 방법을 설명합니다.

## 요구사항
- Docker 24+ 및 docker compose v2
- 로컬 인터넷(Gradle 의존성 다운로드 필요)

## 구성 요소
- `Dockerfile`: 멀티스테이지(Gradle 빌드 → Temurin JRE 런타임)
- `docker/docker-compose.yml`: 로컬 실행용 compose 정의
- `docker/.env.example`: 이미지/포트/프로필 기본값 예시
- 스크립트: `docker/build.sh`, `docker/run.sh`, `docker/stop.sh`, `docker/clean.sh`

## 빠른 시작
1) 환경 변수 설정(선택)
```bash
cp docker/.env.example docker/.env
# 필요 시 값 수정 (IMAGE, TAG, PORT, SPRING_PROFILES_ACTIVE 등)
```

2) 이미지 빌드(테스트 제외)
```bash
./docker/build.sh
```

3) 컨테이너 실행(백그라운드)
```bash
./docker/run.sh
# 로그 팔로우: docker compose --env-file docker/.env -f docker/docker-compose.yml logs -f
```

4) 종료
```bash
./docker/stop.sh
```

5) 이미지 정리
```bash
./docker/clean.sh
```

## 동작 방식
- 빌드 스테이지: `gradle:8.7-jdk21` 이미지에서 `gradle clean build -x test` 실행 후 `build/libs/*.jar` 생성
- 런타임 스테이지: `eclipse-temurin:21-jre` 이미지에서 JAR 실행(`java $JAVA_OPTS -jar /app/app.jar`)
- 기본 포트는 8080 노출. `SPRING_PROFILES_ACTIVE=local` 기본값.

## 자주 쓰는 옵션
- JVM 옵션: `JAVA_OPTS="-Xms256m -Xmx512m"`
- 스프링 프로필: `SPRING_PROFILES_ACTIVE=local` 또는 `dev`, `prod` 등
- 포트 변경: `docker/.env`의 `PORT` 수정

## 트러블슈팅
- Gradle 의존성 다운로드 실패: 네트워크 연결을 확인하거나 VPN/프록시 설정을 적용하세요.
- 포트 충돌: `docker/.env`의 `PORT` 값을 변경하세요.
- 테스트 포함 빌드 필요: `Dockerfile`의 `-x test` 제거 후 재빌드 하세요.

## 참고
- 로컬 실행(비 Docker): `./gradlew bootRun`
- 전체 빌드/테스트: `./gradlew build` / `./gradlew test`
