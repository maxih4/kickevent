# KickEvent backend

Spring Boot backend for the KickEvent event and comment API. The service runs on Java 25, uses MySQL for persistence and authenticates API requests with the existing JWT/refresh-token flow.

## Configuration

Copy `.env.example` to `.env` for a local setup and replace every placeholder value.

| Variable | Required | Default | Description |
| --- | --- | --- | --- |
| `PORT` | no | `8080` | HTTP port exposed by the application |
| `MYSQL_URL` | yes | — | MySQL URL without the `jdbc:` prefix, for example `mysql://localhost:3306/kickevent` |
| `MYSQL_USER` | yes | — | MySQL username |
| `MYSQL_PASSWORD` | yes | — | MySQL password |
| `JWT_SECRET` | yes | — | Base64-encoded HS512 key with at least 64 decoded bytes |
| `FRONTEND_ORIGIN` | no | `http://localhost:3000` | Browser origin allowed by CORS |

The application adds `jdbc:` to `MYSQL_URL`, so a value that already starts with `jdbc:` must not be used. Keep `JWT_SECRET` outside source control; changing it invalidates existing access tokens.

## Run locally

Install JDK 25 and have a reachable MySQL database. Then build and start the packaged application:

```sh
cp .env.example .env
# edit .env
set -a && . ./.env && set +a
mvn -B package
java -jar target/kickeventBackend.jar
```

On PowerShell, load the variables with `Get-Content .env | Where-Object { $_ -and -not $_.StartsWith('#') } | ForEach-Object { $name, $value = $_ -split '=', 2; [Environment]::SetEnvironmentVariable($name, $value) }` or set them in the shell before starting Java. The API is then available at `http://localhost:8080` (or the configured `PORT`).

The repository keeps the existing context test disabled through Maven's `maven.test.skip` setting because it requires a real MySQL/JWT environment and no test database infrastructure is part of this project. The source is still compiled during `mvn package`.

## Run with Docker

The Dockerfile builds the application with Maven and Eclipse Temurin 25, then runs it on the smaller Temurin 25 JRE image as an unprivileged user. No credentials are used during image build.

```sh
docker build -t kickevent-backend .
docker run --rm --env-file .env -p 8080:8080 kickevent-backend
```

Pass `-p <host-port>:${PORT}` if `PORT` is changed. The container listens on `8080` by default and does not contain a keystore; TLS is expected to be terminated by the local reverse proxy or the hosting platform.

## Deploy on Coolify

1. Create a Coolify application from this repository and select **Dockerfile** as the build pack.
2. In **General**, set the Dockerfile path to `/Dockerfile` and expose container port `8080` (or the value chosen for `PORT`). Do not expose the MySQL port publicly.
3. Add the runtime environment variables from the table above in **Environment Variables**. `MYSQL_URL` should use the internal MySQL service hostname and the `mysql://host:3306/database` form. Store `MYSQL_PASSWORD` and `JWT_SECRET` as runtime secrets, never as Docker build arguments.
4. Set `FRONTEND_ORIGIN` to the exact public frontend origin, including the scheme, for example `https://app.example.com`.
5. In **Health Checks**, configure the following values:

   | Coolify field | Value |
   | --- | --- |
   | Type | HTTP |
   | Port | `8080` (or `PORT`) |
   | Path | `/actuator/health` |
   | Scheme | `http` |
   | Interval | `30s` |
   | Timeout | `5s` |
   | Retries | `3` |

6. Configure the Coolify proxy for HTTPS. The application itself serves HTTP only; no certificate or keystore is required in the image.

The health endpoint is public so Coolify can probe it without a JWT. It reports the service as healthy only when the application and its database connection are available. The image also declares the same Docker healthcheck and includes `curl`. Database schema updates are handled by the existing `spring.jpa.hibernate.ddl-auto=update` setting; back up the database before changing it in production.

## Verification

```sh
mvn -B -Dmaven.test.skip=true clean package
```

The build uses Java 25 as configured in `pom.xml`. Tests remain skipped unless a compatible MySQL instance and all required environment variables are supplied.
