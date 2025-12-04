FROM openjdk:21-ea-21-jdk-slim AS build
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

COPY sync-service sync-service

RUN chmod +x gradlew
RUN ./gradlew :sync-service:bootJar --no-daemon

FROM openjdk:21-ea-21-jdk-slim
WORKDIR /app
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
COPY --from=build /app/sync-service/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]