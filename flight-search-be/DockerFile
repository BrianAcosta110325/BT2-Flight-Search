FROM gradle:8.14-jdk24 AS be-builder
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle gradle
COPY src src
RUN gradle build --no-daemon

FROM openjdk:24-jdk-slim
WORKDIR /app
COPY --from=be-builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]