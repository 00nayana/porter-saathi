# Stage 1: Build the application
FROM gradle:8.8-jdk21 AS BUILD_STAGE

WORKDIR /app

# Copy only the build-related files first to leverage Docker layer caching
COPY build.gradle settings.gradle gradlew ./
COPY gradle/ gradle/

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies (this layer will be cached if dependencies don't change)
RUN ./gradlew dependencies

# Copy the rest of the source code
COPY src/ src/

# Build the application
RUN ./gradlew clean bootJar

# Stage 2: Create the final image
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=BUILD_STAGE /app/build/libs/*-SNAPSHOT.jar app.jar

# Expose the port your application listens on (e.g., 8080 for Spring Boot)
EXPOSE 8080

# Define the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]