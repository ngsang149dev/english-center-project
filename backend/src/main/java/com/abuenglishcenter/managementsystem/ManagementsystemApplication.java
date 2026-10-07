package com.abuenglishcenter.managementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling 
public class ManagementsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementsystemApplication.class, args);
	}

}
