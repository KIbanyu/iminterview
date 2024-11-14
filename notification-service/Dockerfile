FROM openjdk:17-jdk-slim
COPY . /app
WORKDIR /app

RUN ./mvnw clean package

COPY target/notification-service-0.0.1.jar /notifcation-service.jar

EXPOSE 8086

CMD ["java", "-Dspring.profiles.active=docker", "-jar", "/notifcation-service.jar"]
