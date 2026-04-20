FROM maven:3.9-eclipse-temurin-21 as builder

WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src/main/ src/main/

RUN mvn clean package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=builder /build/target/*.jar app.jar

RUN chown spring:spring app.jar

USER spring

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
    CMD nc -z localhost 8080 || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
