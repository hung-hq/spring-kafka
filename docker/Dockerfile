FROM maven:3.8.2-openjdk-8 AS builder

WORKDIR /app

COPY ../src /app
COPY pom.xml /app

RUN mvn install -DskipTests

FROM openjdk:8-jdk-alpine

COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]