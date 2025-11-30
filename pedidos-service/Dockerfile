
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .

RUN sed -i '/<modules>/,/<\/modules>/d' pom.xml

RUN mvn -N install

COPY pedidos-service/pom.xml pedidos-service/

RUN mvn dependency:go-offline -f pedidos-service/pom.xml

COPY pedidos-service/src pedidos-service/src

RUN mvn package -DskipTests -f pedidos-service/pom.xml

WORKDIR /app/pedidos-service/target
RUN java -Djarmode=layertools -jar ./*.jar extract

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/pedidos-service/target/dependencies/ ./
COPY --from=builder /app/pedidos-service/target/spring-boot-loader/ ./
COPY --from=builder /app/pedidos-service/target/snapshot-dependencies/ ./
COPY --from=builder /app/pedidos-service/target/application/ ./
EXPOSE 64800
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]