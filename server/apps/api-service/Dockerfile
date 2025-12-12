FROM openjdk:21-ea-21-jdk-slim AS build
WORKDIR /app

COPY ./gradlew .
COPY ./gradle gradle
COPY ./build.gradle.kts .
COPY ./settings.gradle.kts .

COPY ./libs ./libs
COPY ./apps/api-service ./apps/api-service

RUN chmod +x gradlew
RUN ./gradlew :apps:api-service:bootJar --no-daemon

FROM openjdk:21-ea-21-jdk-slim
WORKDIR /app
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
COPY --from=build /app/apps/api-service/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]