FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .

RUN sed -i '/<modules>/,/<\/modules>/d' pom.xml


RUN mvn -N install

COPY historias-clinicas-service/pom.xml historias-clinicas-service/


RUN mvn dependency:go-offline -f historias-clinicas-service/pom.xml

COPY historias-clinicas-service/src historias-clinicas-service/src

RUN mvn package -DskipTests -f historias-clinicas-service/pom.xml


WORKDIR /app/historias-clinicas-service/target
RUN java -Djarmode=layertools -jar ./*.jar extract

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/historias-clinicas-service/target/dependencies/ ./
COPY --from=builder /app/historias-clinicas-service/target/spring-boot-loader/ ./
COPY --from=builder /app/historias-clinicas-service/target/snapshot-dependencies/ ./
COPY --from=builder /app/historias-clinicas-service/target/application/ ./

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]