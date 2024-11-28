package com.example.evenements_categories_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EvenementsCategoriesMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvenementsCategoriesMicroserviceApplication.class, args);
	}

}
