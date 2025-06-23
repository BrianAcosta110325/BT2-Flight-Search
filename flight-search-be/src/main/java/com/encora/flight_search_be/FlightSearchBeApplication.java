package com.encora.flight_search_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FlightSearchBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightSearchBeApplication.class, args);
	}

}