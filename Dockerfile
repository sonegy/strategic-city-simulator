# syntax=docker/dockerfile:1

# ---- Build stage ----
FROM gradle:8.7-jdk21 AS builder
WORKDIR /home/gradle/src

# 캐시 최적화를 위해 빌드 스크립트 먼저 복사
COPY --chown=gradle:gradle settings.gradle build.gradle ./
COPY --chown=gradle:gradle gradle ./gradle
COPY --chown=gradle:gradle gradlew ./

# 소스 복사
COPY --chown=gradle:gradle src ./src

# 테스트는 로컬 빌드 속도를 위해 제외(필요 시 제거)
RUN gradle --no-daemon clean build -x test

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre
ENV SPRING_PROFILES_ACTIVE=local \
    JAVA_OPTS=""

WORKDIR /app
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]

