FROM openjdk:17-jdk-slim
COPY . /app
WORKDIR /app

RUN ./mvnw clean package -DskipTests

COPY target/transaction-service-0.0.1.jar /transaction-service.jar

EXPOSE 8083

CMD ["java", "-jar", "/transaction-service.jar"]
