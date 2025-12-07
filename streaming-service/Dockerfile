FROM maven:3-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:25-ea-21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar ./app.jar
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]