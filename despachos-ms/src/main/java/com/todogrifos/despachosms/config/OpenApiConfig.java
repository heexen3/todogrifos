package com.todogrifos.despachosms.config;

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
    public OpenAPI despachosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("despachos-ms API")
                        .version("1.0")
                        .description("Microservicio de gestión de órdenes de despacho y tracking logístico de envíos")
                        .contact(new Contact()
                                .name("Equipo Backend Todogrifos")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8087")
                                .description("Microservicio Despachos (local)"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("API Gateway (entrada única con JWT)")
                ));
    }
}