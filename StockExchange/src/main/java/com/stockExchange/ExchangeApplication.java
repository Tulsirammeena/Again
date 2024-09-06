package com.stockExchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories("com.stockExchange.repository")
@EnableScheduling
public class ExchangeApplication {


	public static void main(String[] args) {
		SpringApplication.run(ExchangeApplication.class, args);
	}
	

}
