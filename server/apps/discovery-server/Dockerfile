FROM openjdk:21-ea-21-jdk-slim AS build
WORKDIR /app

COPY ./gradlew .
COPY ./gradle gradle
COPY ./build.gradle.kts .
COPY ./settings.gradle.kts .

COPY ./apps/discovery-server ./apps/discovery-server

RUN chmod +x gradlew
RUN ./gradlew :apps:discovery-server:bootJar --no-daemon

FROM openjdk:21-ea-21-jdk-slim
WORKDIR /app
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
COPY --from=build /app/apps/discovery-server/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]