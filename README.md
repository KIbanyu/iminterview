IM Interview Tasks Instructions

For this project, it has 5 services

- Customer service.
- Account service.
- Transaction service.
- Notification service.
- Discovery service.

Steps.


1. Clone the project by running 
 - https://github.com/KIbanyu/iminterview.git


2. To run the project, I have included a docker-compose.yml file. This file will be used to build and run the service in docker.
    Run this command to build the services > docker-compose up --build
    This will deploy kafka, postgres database and all the 5 services.
    All services will be registered in the discovery service. 
    To access discovery service, run this in your browser. http://localhost:8761/eureka.

4. To access individual service exposed for usage.
    Customer service   > http://localhost:8081/swagger-ui/index.html
    Account service   > http://localhost:8082/swagger-ui/index.html
    Transaction service   > http://localhost:8083/swagger-ui/index.html

5. To access the database
    Url  > localhost
    Port > 5433
    Database > im_database
    Username > postgres
    Password > postgres


