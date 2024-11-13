FROM openjdk:17-jdk-slim
COPY . /app
WORKDIR /app

RUN ./mvnw clean package -P docker

COPY target/customer-service-0.0.1.jar /customer-service.jar

EXPOSE 8081

CMD ["java", "-Dspring.profiles.active=docker", "-jar", "/customer-service.jar"]
