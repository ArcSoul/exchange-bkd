# Imagen base de Java
FROM openjdk:17-jdk-alpine
LABEL authors="anthony_rosas"

# Copiamos el JAR generado por Maven
COPY build/libs/exchange-0.0.1-SNAPSHOT.jar app.jar

# Puerto de la API
EXPOSE 8080

# Comando de ejecución
ENTRYPOINT ["java", "-jar", "/app.jar"]
