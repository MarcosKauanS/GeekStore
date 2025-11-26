package com.geekstore.geekstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.geekstore.geekstore")
public class GeekstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeekstoreApplication.class, args);
	}

}
