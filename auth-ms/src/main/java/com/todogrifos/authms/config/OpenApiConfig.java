package com.todogrifos.authms.config;

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
    public OpenAPI authOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("auth-ms API")
                        .version("1.0")
                        .description("Microservicio de autenticación y autorización (JWT) del sistema Todogrifos")
                        .contact(new Contact()
                                .name("Equipo Backend Todogrifos")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8082") // Ajusta el puerto al que use tu auth-ms
                                .description("Microservicio Auth (local)"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("API Gateway")
                ));
    }
}