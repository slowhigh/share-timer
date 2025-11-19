FROM openjdk:21-ea-21-jdk-slim AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew :gateway-server:bootJar --no-daemon

FROM openjdk:21-ea-21-jdk-slim
WORKDIR /app
COPY --from=build /app/gateway-server/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
