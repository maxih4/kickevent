# KickEvent backend

KickEvent is a Spring Boot REST API for events, comments, and user management.

[Open the live demo](https://kickevent.mxhndk.de) · [View the frontend repository](https://github.com/maxih4/KickeventFrontendReact)

## Features

- Event CRUD with search, sorting, and pagination.
- Comment CRUD.
- User registration, login, and token refresh.
- JWT-based authentication with access and refresh tokens.
- Role-based access control for `USER` and `ADMIN`.
- Ownership-based permissions for editing and deleting events and comments.
- A public health endpoint for deployment monitoring.

## Stack and techniques

- Java 25 and Spring Boot
- Spring MVC REST
- Spring Data JPA/Hibernate with MySQL
- Spring Security, BCrypt, and JWT
- Docker for containerization
- Coolify for automated deployment on private infrastructure

## Environment variables

Copy `.env.example` to `.env` and make the variables available to the application. Keep passwords and signing keys outside source control.

| Variable | Required | Default | Description |
| --- | --- | --- | --- |
| `PORT` | No | `8080` | HTTP port used by the application and Docker container. |
| `MYSQL_URL` | Yes | — | MySQL connection in `mysql://host:3306/database` form. The application adds the `jdbc:` prefix, so do not include it here. |
| `MYSQL_USER` | Yes | — | MySQL username. |
| `MYSQL_PASSWORD` | Yes | — | MySQL password. Store it as a runtime secret. |
| `JWT_SECRET` | Yes | — | Base64-encoded signing key with at least 64 decoded bytes for HS512. |
| `FRONTEND_ORIGIN` | No | `http://localhost:3000` | Frontend origin allowed by the backend CORS configuration. |

## Local development

Requirements: Java 25 and a reachable MySQL database.

After configuring the environment variables, build and run the packaged service:

```sh
./mvnw -B package
java -jar target/kickeventBackend.jar
```

On Windows, use `mvnw.cmd` instead of `./mvnw`. The API runs at <http://localhost:8080> by default.

## Deployment

The backend is packaged as a Docker image and deployed automatically through Coolify on private infrastructure. Runtime configuration and secrets are provided through environment variables and are not stored in the repository. Coolify monitors the service through `/actuator/health`.
