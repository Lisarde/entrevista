package com.virtualcave.exercise;

import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.fasterxml.jackson.databind.Module;
import com.virtualcave.exercise.strubs.Stubs;

@SpringBootApplication
@ComponentScan(basePackages = { "com.virtualcave.exercise", "org.openapitools" })
public class EntrevistaApplication {

	public static Stubs stubs = new Stubs();

	public static void main(String[] args) {
		stubs.setUp().stubForGetCurrenciesOut().stubForGetCurrencies("CurrenciesSuccessResponse.json")
				.stubForGetCurrenciesEUR("CurrenciesEURSuccessResponse.json")
				.stubForGetCurrenciesUSD("CurrenciesUSDSuccessResponse.json").status();
		SpringApplication.run(EntrevistaApplication.class, args);
	}

	@Bean(name = "org.openapitools.OpenApiGeneratorApplication.jsonNullableModule")
	public Module jsonNullableModule() {
		return new JsonNullableModule();
	}
}
