package oax.freeman.testcontainers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class TestcontainersApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestcontainersApplication.class, args);
	}

}
