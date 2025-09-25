package com.example.uberbookingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@EntityScan("com.example.Uber_Entity.models")
public class UberbookingserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UberbookingserviceApplication.class, args);
	}

}
