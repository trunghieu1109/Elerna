package com.application.elerna;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.application.elerna.repository")
public class ElernaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElernaApplication.class, args);
	}

}
