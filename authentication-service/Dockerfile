FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .

RUN sed -i '/<modules>/,/<\/modules>/d' pom.xml

RUN mvn -N install

COPY authentication-service/pom.xml authentication-service/

RUN mvn dependency:go-offline -f authentication-service/pom.xml

COPY authentication-service/src authentication-service/src

RUN mvn package -DskipTests -f authentication-service/pom.xml


WORKDIR /app/authentication-service/target
RUN java -Djarmode=layertools -jar ./*.jar extract

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring \

RUN mkdir -p /app/uploads && chown -R spring:spring /app/uploads

USER spring:spring

COPY --from=builder /app/authentication-service/target/dependencies/ ./
COPY --from=builder /app/authentication-service/target/spring-boot-loader/ ./
COPY --from=builder /app/authentication-service/target/snapshot-dependencies/ ./
COPY --from=builder /app/authentication-service/target/application/ ./

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]