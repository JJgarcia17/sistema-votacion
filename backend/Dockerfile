# ----- Etapa 1: Build (Construcción) -----
# Usamos una imagen de Maven con Java 17 (Eclipse Temurin es una gran opción)
FROM maven:3.9-eclipse-temurin-17 AS builder

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos solo el pom.xml primero para aprovechar la caché de Docker
# Si el pom.xml no cambia, Docker no volverá a descargar las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el resto del código fuente y construimos la aplicación
COPY src ./src
RUN mvn package -DskipTests

# ----- Etapa 2: Run (Ejecución) -----
# Usamos una imagen de JRE (Java Runtime Environment) mucho más ligera para producción
FROM eclipse-temurin:17-jre-jammy

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el .jar construido desde la etapa 'builder'
COPY --from=builder /app/target/*.jar app.jar

# Exponemos el puerto en el que correrá Spring Boot (por defecto 8080)
EXPOSE 8080

# Comando para ejecutar la aplicación cuando se inicie el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]