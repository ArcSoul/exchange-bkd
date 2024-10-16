package com.tata.ws.exchange.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Servidor de la API
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Servidor local para desarrollo"))
                .addServersItem(new Server()
                        .url("https://api.exchange.com")
                        .description("Servidor de producción"))

                // Requisitos de seguridad
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))

                // Componentes de seguridad (JWT)
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Autenticación JWT con el esquema Bearer para acceder a los endpoints protegidos")))

                // Información detallada de la API
                .info(new Info()
                        .title("API de Tipo de Cambio y Autenticación")
                        .version("1.0.0")
                        .description("Esta API permite realizar conversiones de tipo de cambio entre diversas monedas y gestionar la autenticación de usuarios.\n\n"
                                + "Características:\n"
                                + "- Aplicar tipo de cambio.\n"
                                + "- Consultar tipos de cambio disponibles.\n"
                                + "- Autenticación de usuarios mediante JWT.\n"
                                + "- Recuperación y restablecimiento de contraseña.\n")
                        .termsOfService("https://www.exchange.com/terminos")
                        .contact(new Contact()
                                .name("Soporte API")
                                .url("https://www.exchange.com/soporte")
                                .email("soporte@tucambioapp.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
