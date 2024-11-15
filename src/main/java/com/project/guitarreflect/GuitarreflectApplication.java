package com.project.guitarreflect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GuitarreflectApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuitarreflectApplication.class, args);
	}

}
