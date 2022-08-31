package oax.freeman.testcontainers;

import com.rabbitmq.client.AMQP;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class TestInfrastructure {

    @Container
    public static RabbitMQContainer rabbitmqContainer = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.7.25-management-alpine"));

    public static AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
            .contentType("application/json")
            .contentEncoding("UTF-8").build();

    @Container
    public static PostgreSQLContainer postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse("postgres:11.1"))
            .withDatabaseName("test")
            .withUsername("user")
            .withPassword("pass")
            .withInitScript("init-test-db.sql");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.username", rabbitmqContainer::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitmqContainer::getAdminPassword);
        registry.add("spring.rabbitmq.host", rabbitmqContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitmqContainer::getAmqpPort);

        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);

    }
}
