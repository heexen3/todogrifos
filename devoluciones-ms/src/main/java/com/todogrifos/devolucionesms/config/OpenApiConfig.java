package com.todogrifos.devolucionesms.config;

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
    public OpenAPI devolucionesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("devoluciones-ms API")
                        .version("1.0")
                        .description("Microservicio de gestión de notas de crédito, devoluciones y reingreso o merma de inventario")
                        .contact(new Contact()
                                .name("Equipo Backend Todogrifos")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8091")
                                .description("Microservicio Devoluciones (local)"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("API Gateway (entrada única con JWT)")
                ));
    }
}