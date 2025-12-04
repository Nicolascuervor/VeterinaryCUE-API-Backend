FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .
RUN sed -i '/<modules>/,/<\/modules>/d' pom.xml && \
    mvn -N install

COPY agendamiento-service/pom.xml agendamiento-service/ \
     agendamiento-service/src agendamiento-service/src

RUN mvn install -DskipTests -Dspring-boot.repackage.skip=true -f agendamiento-service/pom.xml

COPY citas-service/pom.xml citas-service/

RUN mvn dependency:go-offline -f citas-service/pom.xml

COPY citas-service/src citas-service/src

RUN mvn package -DskipTests -f citas-service/pom.xml


WORKDIR /app/citas-service/target
RUN java -Djarmode=layertools -jar ./*.jar extract

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/citas-service/target/dependencies/ ./
COPY --from=builder /app/citas-service/target/spring-boot-loader/ ./
COPY --from=builder /app/citas-service/target/snapshot-dependencies/ ./
COPY --from=builder /app/citas-service/target/application/ ./

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]