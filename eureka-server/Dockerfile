FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .

RUN sed -i '/<modules>/,/<\/modules>/d' pom.xml


RUN mvn -N install

COPY eureka-server/pom.xml eureka-server/

RUN mvn dependency:go-offline -f eureka-server/pom.xml

COPY eureka-server/src eureka-server/src

RUN mvn package -DskipTests -f eureka-server/pom.xml

WORKDIR /app/eureka-server/target
RUN java -Djarmode=layertools -jar ./*.jar extract

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/eureka-server/target/dependencies/ ./
COPY --from=builder /app/eureka-server/target/spring-boot-loader/ ./
COPY --from=builder /app/eureka-server/target/snapshot-dependencies/ ./
COPY --from=builder /app/eureka-server/target/application/ ./

EXPOSE 8761

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]