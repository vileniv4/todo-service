FROM bellsoft/liberica-openjdk-alpine:17 AS builder

WORKDIR /application

COPY .mvn ./.mvn
COPY mvnw ./mvnw
RUN sed -i 's/\r$//' ./mvnw && chmod +x ./mvnw

COPY pom.xml .
RUN ./mvnw dependency:go-offline -B || true

COPY src ./src
RUN ./mvnw clean package -DskipTests -B

RUN java -Djarmode=layertools -jar target/todo-service-*.jar extract --destination /extracted

FROM bellsoft/liberica-openjdk-alpine:17

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /application

COPY --from=builder /extracted/dependencies/ ./
COPY --from=builder /extracted/spring-boot-loader/ ./
COPY --from=builder /extracted/snapshot-dependencies/ ./
COPY --from=builder /extracted/application/ ./

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", \
            "-XX:+UseContainerSupport", \
            "-XX:MaxRAMPercentage=75.0", \
            "org.springframework.boot.loader.launch.JarLauncher"]