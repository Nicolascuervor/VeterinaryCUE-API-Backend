FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .

RUN sed -i '/<modules>/,/<\/modules>/d' pom.xml

RUN mvn -N install

COPY notification-service/pom.xml notification-service/
RUN mvn dependency:go-offline -f notification-service/pom.xml

COPY notification-service/src notification-service/src
RUN mvn package -DskipTests -f notification-service/pom.xml

WORKDIR /app/notification-service/target
RUN java -Djarmode=layertools -jar ./*.jar extract

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/notification-service/target/dependencies/ ./
COPY --from=builder /app/notification-service/target/spring-boot-loader/ ./
COPY --from=builder /app/notification-service/target/snapshot-dependencies/ ./
COPY --from=builder /app/notification-service/target/application/ ./

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]