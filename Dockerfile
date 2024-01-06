FROM maven AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package spring-boot:repackage -f pom.xml

FROM amazoncorretto:11
EXPOSE 8443:8443
WORKDIR /app
COPY --from=build /app/target/kickeventBackend.jar /app/kickeventBackend.jar



ENTRYPOINT ["java","-jar","/app/kickeventBackend.jar"]

