# Stage 1: Build
FROM clojure:temurin-21-lein AS builder
WORKDIR /app
COPY . .
RUN lein uberjar

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*-standalone.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]