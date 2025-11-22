FROM openjdk:21-ea-21-jdk-slim AS build
WORKDIR /app

# 1. Gradle Wrapper 및 프로젝트 설정 파일만 먼저 복사 (캐시 활용)
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# 2. 해당 서비스의 소스코드만 복사
COPY timer-service timer-service

# 3. 빌드 실행
RUN chmod +x gradlew
RUN ./gradlew :timer-service:bootJar --no-daemon

FROM openjdk:21-ea-21-jdk-slim
WORKDIR /app
COPY --from=build /app/timer-service/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]