
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .

RUN sed -i '/<modules>/,/<\/modules>/d' pom.xml


RUN mvn -N install


COPY inventario-service/pom.xml inventario-service/

RUN mvn dependency:go-offline -f inventario-service/pom.xml

COPY inventario-service/src inventario-service/src

RUN mvn package -DskipTests -f inventario-service/pom.xml


WORKDIR /app/inventario-service/target
RUN java -Djarmode=layertools -jar ./*.jar extract

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring && \
    mkdir -p /app/uploads/productos && \
    chown -R spring:spring /app/uploads

COPY --from=builder /app/inventario-service/target/dependencies/ ./
COPY --from=builder /app/inventario-service/target/spring-boot-loader/ ./
COPY --from=builder /app/inventario-service/target/snapshot-dependencies/ ./
COPY --from=builder /app/inventario-service/target/application/ ./

USER spring:spring

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]