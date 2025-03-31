package org.healthystyle.health.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "org.healthystyle.health")
@EnableJpaRepositories(basePackages = "org.healthystyle.health.repository")
@EntityScan(basePackages = "org.healthystyle.health.model")
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
