FROM maven AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN clean package spring-boot:repackage -f pom.xml

FROM amazoncorretto:11
EXPOSE 8443:8443
WORKDIR /app
COPY --from=build /app/target/kickeventBackend.jar /app/kickeventBackend.jar


ENV JWT_SECRET=${{secrets.JWT_SECRET}}
ENV KEYSTORE_PASS=${{secrets.KEYSTORE_PASS}}
ENV MYSQL_PASSWORD=${{secrets.MYSQL_PASSWORD}}
ENV MYSQL_USER=${{secrets.MYSQL_USER}}
ENTRYPOINT ["java","-jar","/app/kickeventBackend.jar"]