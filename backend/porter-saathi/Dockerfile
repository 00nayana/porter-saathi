# Stage 1: Build the application
FROM gradle:8.8-jdk21 AS BUILD_STAGE

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle/ gradle/
RUN chmod +x gradlew

# Copy source code
COPY src/ src/

# Build the Spring Boot fat jar (skip tests for faster and more predictable build)
RUN ./gradlew clean bootJar -x test

# Verify jar contains main class (optional debug for Render logs)
# RUN jar tf build/libs/app.jar | grep PorterSaathiApplication

# Stage 2: Runtime
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the built fat jar explicitly
COPY --from=BUILD_STAGE /app/build/libs/app.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]