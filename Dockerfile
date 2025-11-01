# ----------- STAGE 1: Build the JAR -----------
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copy pom.xml first to cache dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the project (skip tests for faster builds)
RUN mvn clean package -DskipTests

# ----------- STAGE 2: Run the JAR -----------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Set environment variable for Spring Boot port (Render injects $PORT)
ENV PORT=8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]