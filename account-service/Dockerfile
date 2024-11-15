FROM openjdk:17-jdk-slim
COPY . /app
WORKDIR /app

RUN ./mvnw clean package

COPY target/account-service-0.0.1.jar /account-service.jar

EXPOSE 8082

CMD ["java", "-jar", "/account-service.jar"]
