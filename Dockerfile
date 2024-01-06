FROM maven:3.6.0-amazoncorretto-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN --mount=type=secret,id=JWT_SECRET \
    --mount=type=secret,id=KEYSTORE_PASS \
    --mount=type=secret,id=MYSQL_PASSWORD \
    --mount=type=secret,id=MYSQL_USER \
    export JWT_SECRET=$(cat /run/secrets/JWT_SECRET) \
    export KEYSTORE_PASS=$(cat /run/secrets/KEYSTORE_PASS) && \
    export MYSQL_PASSWORD=$(cat /run/secrets/MYSQL_PASSWORD) && \
    export MYSQL_USER=$(cat /run/secrets/MYSQL_USER) && \
    mvn clean package spring-boot:repackage -f pom.xml

FROM amazoncorretto:11
EXPOSE 8443:8443
WORKDIR /app
COPY --from=build /app/target/kickeventBackend.jar /app/kickeventBackend.jar



ENTRYPOINT ["java","-jar","/app/kickeventBackend.jar"]

