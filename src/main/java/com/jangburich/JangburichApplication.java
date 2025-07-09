package com.jangburich;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class JangburichApplication {

	public static void main(String[] args) {
		SpringApplication.run(JangburichApplication.class, args);
	}

}
