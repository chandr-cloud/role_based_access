package com.nt.config;

import org.springframework.beans.factory.annotation.Value;
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

/**
 * OpenAPI/Swagger configuration for API documentation.
 * Includes JWT bearer authentication for secured APIs.
 */
@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private int serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        String serverUrl = "http://localhost:" + serverPort;
        
        // Build info
        Info info = new Info();
        info.setTitle("Library Management API");
        info.setVersion("1.0");
        info.setDescription("REST API for Library Management System with JWT Authentication");
        
        Contact contact = new Contact();
        contact.setName("API Support");
        contact.setEmail("support@library.com");
        info.setContact(contact);
        
        License license = new License();
        license.setName("Apache 2.0");
        license.setUrl("https://www.apache.org/licenses/LICENSE-2.0");
        info.setLicense(license);
        
        // Build server
        Server server = new Server();
        server.setUrl(serverUrl);
        server.setDescription("Local server");
        
        // Build security scheme
        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.setType(SecurityScheme.Type.HTTP);
        securityScheme.setScheme("bearer");
        securityScheme.setBearerFormat("JWT");
        securityScheme.setDescription("Enter JWT token");
        
        Components components = new Components();
        components.addSecuritySchemes("Bearer Authentication", securityScheme);
        
        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("Bearer Authentication");
        
        // Build OpenAPI
        OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(info);
        openAPI.addServersItem(server);
        openAPI.setComponents(components);
        openAPI.addSecurityItem(securityRequirement);
        
        return openAPI;
    }
}
