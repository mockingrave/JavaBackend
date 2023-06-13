package ru.mockingrave.ethereum.javabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(GethProperties.class)
public class JavaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaBackendApplication.class, args);

	}

}