FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8084
EXPOSE 8091
ENTRYPOINT ["java", "-jar", "app.jar"]
