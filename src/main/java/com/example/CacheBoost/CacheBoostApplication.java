package com.example.CacheBoost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableJpaAuditing
public class CacheBoostApplication {

	public static void main(String[] args) {
		SpringApplication.run(CacheBoostApplication.class, args);
	}

}
