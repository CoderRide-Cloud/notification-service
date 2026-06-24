# Build from monorepo root: docker build -f notification-service/Dockerfile -t notification-service .
# syntax=docker/dockerfile:1
FROM eclipse-temurin:21-jdk-alpine AS build
RUN apk add --no-cache maven
WORKDIR /workspace
COPY common-lib ./common-lib
RUN cd common-lib && mvn clean install -DskipTests -q
COPY notification-service/pom.xml ./notification-service/pom.xml
COPY notification-service/src ./notification-service/src
RUN cd notification-service && mvn clean package -DskipTests -q
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /workspace/notification-service/target/*.jar app.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "app.jar"]
