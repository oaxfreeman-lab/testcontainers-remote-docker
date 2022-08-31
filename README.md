# Testcontainers with remote docker host configuration
### Project for [RabbitMQ, PostgreSQL , Spring Cloud Streams, Testcontainers and Remote Docker Host — Integration Test Story Article](https://medium.com/@oaxfreeman/rabbitmq-postgresql-spring-cloud-streams-testcontainers-and-remote-docker-host-integration-4594c20d20b9)
#### Fast run
```
./mvn test 
```
but it's best you import it to your favourite IDE.
#### Know issues:
Testcontainers sometimes do not pick up `testcontainer.properties` from `classpath`, 
which is neccessary for remote docker execution.
It will fall back to your local docker installation.
If you still want to try remote execution refer to the 5 step of [the article](https://medium.com/@oaxfreeman/rabbitmq-postgresql-spring-cloud-streams-testcontainers-and-remote-docker-host-integration-4594c20d20b9).
