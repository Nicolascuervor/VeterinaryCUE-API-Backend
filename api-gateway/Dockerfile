FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .

RUN sed -i '/<modules>/,/<\/modules>/d' pom.xml


RUN mvn -N install

COPY api-gateway/pom.xml api-gateway/


RUN mvn dependency:go-offline -f api-gateway/pom.xml


COPY api-gateway/src api-gateway/src

RUN mvn package -DskipTests -f api-gateway/pom.xml


WORKDIR /app/api-gateway/target
RUN java -Djarmode=layertools -jar ./*.jar extract

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/api-gateway/target/dependencies/ ./
COPY --from=builder /app/api-gateway/target/spring-boot-loader/ ./
COPY --from=builder /app/api-gateway/target/snapshot-dependencies/ ./
COPY --from=builder /app/api-gateway/target/application/ ./

EXPOSE 8070

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]