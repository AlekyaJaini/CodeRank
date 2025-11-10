# stage: build
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

# stage: runtime
FROM eclipse-temurin:17-jdk-jammy
# install docker CLI (and tini to handle PID 1 signals)
RUN apt-get update \
 && apt-get install -y --no-install-recommends docker.io tini ca-certificates curl \
 && rm -rf /var/lib/apt/lists/*

# create app user (don't run as root)
RUN useradd -m -u 10001 appuser || true
WORKDIR /app

# copy jar from build stage
COPY --from=build /workspace/target/*.jar app.jar
# give jar to appuser
RUN chown -R appuser:appuser /app

ENV JAVA_OPTS="-Xms256m -Xmx512m"
EXPOSE 8080

# use tini to reap processes
ENTRYPOINT ["/usr/bin/tini", "--", "sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
USER appuser
