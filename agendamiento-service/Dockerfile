FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# 1. Copiamos el POM padre
COPY pom.xml .

# [FIX] Eliminamos los módulos para romper la dependencia de las carpetas que no existen
RUN sed -i '/<modules>/,/<\/modules>/d' pom.xml

# [NUEVO] Instalamos el POM padre en el repo local (.m2) del contenedor.
# La opción -N (Non-recursive) es clave para que no busque hijos.
RUN mvn -N install

# 2. Copiamos el POM del microservicio específico
COPY agendamiento-service/pom.xml agendamiento-service/

# 3. Descargamos dependencias
# [CAMBIO CLAVE] Entramos al directorio del hijo (-f) para descargar SUS dependencias.
# Ya no usamos -pl ni -am porque estamos operando directamente sobre el pom del hijo.
RUN mvn dependency:go-offline -f agendamiento-service/pom.xml

# 4. Copiamos el código fuente
COPY agendamiento-service/src agendamiento-service/src

# 5. Compilamos
# [CAMBIO CLAVE] Compilamos directamente el hijo.
RUN mvn package -DskipTests -f agendamiento-service/pom.xml

# 6. Extraemos capas
WORKDIR /app/agendamiento-service/target
RUN java -Djarmode=layertools -jar ./*.jar extract

# ==========================================
# Etapa 2: Runner
# ==========================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/agendamiento-service/target/dependencies/ ./
COPY --from=builder /app/agendamiento-service/target/spring-boot-loader/ ./
COPY --from=builder /app/agendamiento-service/target/snapshot-dependencies/ ./
COPY --from=builder /app/agendamiento-service/target/application/ ./

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]