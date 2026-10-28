# spring-boot-docker-jib

Spring Boot 3 REST service (Java 21, Maven) that can be packaged as a container image with [Jib](https://github.com/GoogleContainerTools/jib).

## Build & run

```bash
mvn verify                      # compile + tests
mvn spring-boot:run             # run on http://localhost:8080
mvn compile jib:dockerBuild     # build image into local Docker daemon
docker run -p 8080:8080 spring-boot-docker-jib:0.0.1-SNAPSHOT
```

## Endpoints

| Method | Path                         | Description              |
|--------|------------------------------|--------------------------|
| GET    | `/api/greetings/hello?name=` | Simple hello message     |
| GET    | `/api/greetings`             | List greetings           |
| GET    | `/api/greetings/{id}`        | Get greeting by id       |
| POST   | `/api/greetings`             | Create `{"message":"…"}` |
| DELETE | `/api/greetings/{id}`        | Delete greeting          |
| POST   | `/api/greetings-stream`      | Batch create `[{"message":"…"}]`; streams NDJSON |
| POST   | `/api/greetings-async`       | Create `{"message":"…"}` asynchronously |
| GET    | `/actuator/health`           | Health check             |

CI: `.github/workflows/build.yml` runs `mvn verify` on every push/PR to `main`.
