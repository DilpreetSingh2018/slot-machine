# ------------ Build Stage ------------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


# ------------ Runtime Stage ------------
FROM eclipse-temurin:17-jre
WORKDIR /app
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
