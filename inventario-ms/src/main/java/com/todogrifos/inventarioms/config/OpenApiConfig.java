package com.todogrifos.inventarioms.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI inventarioOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("inventario-ms API")
                        .version("1.0")
                        .description("Microservicio de control de stock por SKU, validación de disponibilidad y gestión de inventario")
                        .contact(new Contact()
                                .name("Equipo Backend Todogrifos")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8089")
                                .description("Microservicio Inventario (local)"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("API Gateway (entrada única con JWT)")
                ));
    }
}