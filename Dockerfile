FROM maven:3.9.1-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:19
COPY --from=build /target/spreeze-0.0.1-SNAPSHOT.jar spreeze-0.0.1-SNAPSHOT.jar
    
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "spreeze-0.0.1-SNAPSHOT.jar"]