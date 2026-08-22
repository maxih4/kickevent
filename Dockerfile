FROM maven:3.9.11-eclipse-temurin-25 AS build
WORKDIR /workspace

COPY pom.xml .
RUN mvn -B -ntp dependency:go-offline

COPY src ./src
RUN mvn -B -ntp package

FROM eclipse-temurin:25-jre
WORKDIR /app

RUN groupadd --system spring && useradd --system --gid spring spring
COPY --from=build /workspace/target/kickeventBackend.jar /app/kickeventBackend.jar

USER spring
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/kickeventBackend.jar"]

