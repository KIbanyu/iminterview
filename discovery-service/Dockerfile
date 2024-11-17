FROM openjdk:17-jdk-slim
COPY . /app
WORKDIR /app

RUN ./mvnw clean package -DskipTests

COPY target/discovery-service-0.0.1.jar /discovery-service.jar

EXPOSE 9090

CMD ["java", "-jar", "/discovery-service.jar"]