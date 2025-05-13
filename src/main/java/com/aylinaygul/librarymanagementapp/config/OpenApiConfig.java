package com.aylinaygul.librarymanagementapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI().info(new Info().title("Library Management App")
                                .description("API documentation for managing library system")
                                .contact(new Contact().name("Aylin").email("aylnaygul2@gmail.com")))
                                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                                .schemaRequirement("bearerAuth", new SecurityScheme()
                                                .name("bearerAuth").type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer").bearerFormat("JWT")
                                                .in(SecurityScheme.In.HEADER)
                                                .description("JWT auth description"));
        }
}
