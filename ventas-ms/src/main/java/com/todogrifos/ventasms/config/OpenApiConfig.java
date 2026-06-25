package com.todogrifos.ventasms.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ventasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ventas-ms API")
                        .version("1.0")
                        .description("Microservicio de registro de ventas, emisión de boletas y descuento automático de stock")
                        .contact(new Contact()
                                .name("Equipo Backend Todogrifos")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8084")
                                .description("Microservicio Ventas (local)"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("API Gateway (entrada única con JWT)")
                ));
    }
}