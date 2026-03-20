package com.nt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI apiInfo() {
		return new OpenAPI()
				.info(new Info()
						.title("Library Management API")
						.description("REST API for Library Management System with JWT Authentication")
						.version("1.0.0")
						.contact(new Contact()
								.name("Your Name")
								.email("your.email@example.com"))
						.license(new License()
								.name("MIT License")
								.url("https://opensource.org/licenses/MIT")))
				.servers(java.util.List.of(
						new Server().url("http://localhost:8080").description("Local Development Server")))
				.components(new Components()
						.addSecuritySchemes("bearerAuth",
								new SecurityScheme()
										.type(SecurityScheme.Type.HTTP)
										.scheme("bearer")
										.bearerFormat("JWT")
										.description("JWT Authorization header using the Bearer scheme. Example: \"Authorization: Bearer {token}\"")))
				.security(java.util.List.of(new SecurityRequirement().addList("bearerAuth")));
	}
// http://localhost:8080/swagger-ui/index.html

}
