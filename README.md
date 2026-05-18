# TodoGrifos - Arquitectura de Microservicios

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen.svg)
![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-Eureka_|_Gateway-blue.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)
![Security](https://img.shields.io/badge/Security-JWT-yellow.svg)

**TodoGrifos** es un sistema integral de gestión enfocado en el sector retail (ferretería/grifería), diseñado bajo una arquitectura de **Microservicios** altamente escalable, resiliente y segura.

## Arquitectura del Sistema

El ecosistema está compuesto por **12 componentes principales**: 2 de infraestructura core y 10 microservicios de negocio independientes.

### Infraestructura Core
* **`eureka-server` (8761):** Servidor de descubrimiento (Service Registry). Mantiene el registro dinámico de todos los nodos activos.
* **`api-gateway` (8080):** Enrutador centralizado (Spring Cloud Gateway) construido sobre WebFlux. Implementa un `AuthenticationFilter` reactivo que actúa como escudo de seguridad perimetral validando tokens JWT.

### Microservicios de Negocio
Cada microservicio posee su propia capa de persistencia (Base de Datos aislada), validaciones (JSR-380) y manejo centralizado de excepciones (`GlobalExceptionHandler`). La comunicación síncrona entre dominios se realiza mediante **OpenFeign**.

1. **`auth-ms` (8082):** Gestión de identidades (Login/Registro), encriptación y firma de tokens JWT.
2. **`productos-ms`:** Catálogo central (SKU, categorías, marcas).
3. **`clientes-ms`:** Gestión del perfil de compradores.
4. **`proveedores-ms`:** Administración de entidades abastecedoras.
5. **`vendedores-ms`:** Personal de atención y comisiones.
6. **`inventario-ms`:** Control estricto de existencias físicas.
7. **`ventas-ms`:** Procesamiento transaccional de boletas y facturas.
8. **`compras-ms`:** Gestión de órdenes de compra institucionales.
9. **`despachos-ms`:** Logística de envíos y seguimiento.
10. **`devoluciones-ms`:** Flujo inverso logístico (Notas de Crédito y reingresos a stock).

## Tecnologías y Herramientas

* **Backend:** Java 21, Spring Boot (Data JPA, Web, Security, WebFlux)
* **Cloud:** Spring Cloud Netflix Eureka, Spring Cloud Gateway, OpenFeign
* **Seguridad:** JSON Web Tokens (JWT)
* **Base de Datos:** MySQL Connector
* **Validaciones:** JSR-380 (Hibernate Validator), Lombok

## Configuración y Ejecución Local

### Orden de Despliegue Estricto
Para asegurar que el Service Registry y el API Gateway reconozcan todos los nodos sin errores, levanta los servicios en este orden:

1. Iniciar **`eureka-server`** (Puerto `8761`).
2. Iniciar **`api-gateway`** (Puerto `8080`).
3. Iniciar **`auth-ms`** (Puerto `8082`).
4. Iniciar los **9 microservicios restantes** (El orden entre ellos no importa).

## Seguridad y Flujo de Peticiones

* **Endpoints Públicos:** `/api/auth/register` y `/api/auth/login`.
* **Endpoints Protegidos:** El Gateway intercepta la petición, verifica el header `Authorization: Bearer <token>` comprobando su firma y expiración, inyecta la cabecera `X-Auth-User` y delega la petición a los microservicios de negocio.

## Ejemplos de Consumo (Postman)

**1. Obtener Token (Público)**
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
    "username": "usuario_demo",
    "password": "passwordSeguro1"
}
