package com.company.customer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfiguration {

	@Bean
	public OpenAPI apiInfo() {
		return new OpenAPI().info(new Info().title("Customer Service API documentation")
				.description("Documentation for Customer Service API").version("v1").contact(getContactDetails()));
	}

	private Contact getContactDetails() {
		return new Contact().name("Arun Garimella").email("arun.12392@gmail.com").url("");
	}

}
