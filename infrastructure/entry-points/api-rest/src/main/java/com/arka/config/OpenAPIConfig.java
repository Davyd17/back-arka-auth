package com.arka.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${api.base-url}")
    String apiBaseUrl;

    @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemeName = "bearerAuth";

        Server server = new Server();
        server.setUrl(apiBaseUrl);
        server.description("Open API Documentation");


        Contact contact = new Contact();
        contact.name("David Correa");
        contact.email("davidcq55@gmail.com");

        Info info = new Info()
                .title("Arka Auth")
                .description("Proyecto de autenticacion de servicios ARKA")
                .contact(contact)
                .version("1.0")
                .license(new License().name("Apache 2.0").url("http://localhost"));

        SecurityRequirement security = new SecurityRequirement()
                .addList(securitySchemeName);

        Components components = new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Enter your JWT token directly into the field below."));


        return new OpenAPI()
                .info(info)
                .servers(List.of(server))
                .addSecurityItem(security)
                .components(components);
    }

}
