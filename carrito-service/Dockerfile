FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .

RUN sed -i '/<modules>/,/<\/modules>/d' pom.xml

RUN mvn -N install

COPY carrito-service/pom.xml carrito-service/

RUN mvn dependency:go-offline -f carrito-service/pom.xml


COPY carrito-service/src carrito-service/src

RUN mvn package -DskipTests -f carrito-service/pom.xml


WORKDIR /app/carrito-service/target
RUN java -Djarmode=layertools -jar ./*.jar extract

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/carrito-service/target/dependencies/ ./
COPY --from=builder /app/carrito-service/target/spring-boot-loader/ ./
COPY --from=builder /app/carrito-service/target/snapshot-dependencies/ ./
COPY --from=builder /app/carrito-service/target/application/ ./

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]