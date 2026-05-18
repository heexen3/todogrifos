package com.todogrifos.api_gateway.filter;

import com.todogrifos.api_gateway.config.JwtUtils;
import com.todogrifos.api_gateway.config.RouterValidator;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component("AuthenticationFilter")
@RefreshScope
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouterValidator routerValidator;

    @Autowired
    private JwtUtils jwtUtils;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // verificar si la ruta requiere validación de token
            if (routerValidator.isSecured.test(request)) {
                log.info("Ruta protegida detectada por el Gateway: {}", request.getURI().getPath());

                // verificar la existencia de la cabecera Authorization
                if (!request.getHeaders().containsKey("Authorization")) {
                    log.warn("Acceso denegado: Cabecera Authorization ausente en la petición.");
                    return onError(exchange, "Cabecera de autorización faltante", HttpStatus.UNAUTHORIZED);
                }

                String authHeader = request.getHeaders().getOrEmpty("Authorization").get(0);

                // validar el formato de tipo Bearer
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    log.warn("Acceso denegado: Formato de token inválido (Debe ser Bearer).");
                    return onError(exchange, "Formato de token inválido", HttpStatus.UNAUTHORIZED);
                }

                String token = authHeader.substring(7);

                // validar firma y expiración del JWT
                if (jwtUtils.isExpired(token)) {
                    log.warn("Acceso denegado: El token JWT provisto ha expirado o es corrupto.");
                    return onError(exchange, "Token JWT inválido o expirado", HttpStatus.UNAUTHORIZED);
                }

                // Inyectar metadatos del usuario autenticado (username/email) a los microservicios aguas abajo
                Claims claims = jwtUtils.getClaims(token);
                request = exchange.getRequest().mutate()
                        .header("X-Auth-User", claims.getSubject())
                        .build();

                log.info("Token válido. Usuario '{}' autorizado por el Gateway.", claims.getSubject());
                return chain.filter(exchange.mutate().request(request).build());
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        // Retorna una respuesta vacía con el código de estado 401 Unauthorized
        return response.setComplete();
    }

    public static class Config {
        // Clase de configuración requerida por el Factory de Spring Cloud Gateway
    }
}