FROM openjdk:21-ea-21-jdk-slim AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew :discovery-server:bootJar --no-daemon

FROM openjdk:21-ea-21-jdk-slim
WORKDIR /app
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
COPY --from=build /app/discovery-server/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
