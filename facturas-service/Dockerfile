
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .

RUN sed -i '/<modules>/,/<\/modules>/d' pom.xml

RUN mvn -N install

COPY facturas-service/pom.xml facturas-service/

RUN mvn dependency:go-offline -f facturas-service/pom.xml


COPY facturas-service/src facturas-service/src

RUN mvn package -DskipTests -f facturas-service/pom.xml


WORKDIR /app/facturas-service/target
RUN java -Djarmode=layertools -jar ./*.jar extract

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/facturas-service/target/dependencies/ ./
COPY --from=builder /app/facturas-service/target/spring-boot-loader/ ./
COPY --from=builder /app/facturas-service/target/snapshot-dependencies/ ./
COPY --from=builder /app/facturas-service/target/application/ ./

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]