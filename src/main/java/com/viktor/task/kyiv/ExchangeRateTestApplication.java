package com.viktor.task.kyiv;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling
public class ExchangeRateTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeRateTestApplication.class, args);
	}

}
