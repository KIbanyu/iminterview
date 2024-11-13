FROM openjdk:17-jdk-slim
COPY . /app
WORKDIR /app

RUN ./mvnw clean package -DskipTests

COPY target/account-service-0.0.1.jar /account-service.jar

EXPOSE 8081

CMD ["java", "-Dspring.profiles.active=docker", "-jar", "/account-service.jar"]
