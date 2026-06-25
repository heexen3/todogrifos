package com.todogrifos.productosms.config;

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
    public OpenAPI productosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("productos-ms API")
                        .version("1.0")
                        .description("Microservicio de catálogo de productos, marcas y categorías")
                        .contact(new Contact()
                                .name("Equipo Backend Todogrifos")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8081")
                                .description("Microservicio Productos (local)"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("API Gateway (entrada única con JWT)")
                ));
    }
}