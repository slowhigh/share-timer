FROM openjdk:21-jdk-slim AS build

WORKDIR /app

COPY . /app/

RUN ./mvnw clean package -P dev -D skipTests 

FROM openjdk:21-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 5000

ENTRYPOINT ["java","-jar","app.jar"]