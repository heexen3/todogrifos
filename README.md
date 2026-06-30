# TodoGrifos - Sistema de Microservicios

TodoGrifos es una plataforma multi-módulo para la gestión integral de una tienda de **grifería y ferretería**. Permite administrar la autenticación de usuarios, catálogo de productos, clientes, ventas, compras, inventario, proveedores, despachos, comisiones de vendedores y devoluciones (notas de crédito) a través de una arquitectura de microservicios desarrollada con Spring Boot y Spring Cloud.

Este proyecto está diseñado para trabajar con Eureka Server, API Gateway, comunicación síncrona por OpenFeign, bases de datos independientes por servicio (MySQL), seguridad mediante Spring Security + JWT, pruebas unitarias aisladas (JUnit 5 + Mockito) y documentación OpenAPI con Swagger.

---

# 1. Objetivo del proyecto

El sistema automatiza el flujo operativo completo de la ferretería y distribuidora:

1. **Autenticación**: Registro y login de usuarios con generación de tokens JWT seguros.
2. **Catálogo**: Administración de productos agrupados por marcas y categorías.
3. **Clientes y Proveedores**: Registro, datos fiscales (RUT) e información de contacto.
4. **Inventario**: Control de stock de almacén por SKU, permitiendo incrementos y decrementos (retiros).
5. **Transacciones**:
   * **Compras**: Órdenes de abastecimiento a proveedores que incrementan stock en bodega.
   * **Ventas**: Emisión de boletas a clientes que decrementan stock en bodega.
6. **Logística**: Creación y seguimiento de despachos asociados a ventas.
7. **Personal**: Control de vendedores y cálculo de comisiones acumuladas.
8. **Post-venta**: Gestión de devoluciones (Notas de Crédito) con reingreso automático al inventario o registro de mermas.

---

# 2. Arquitectura general

```text
Cliente externo / Postman / Navegador / Frontend
        |
        v
API Gateway :8080 (Filtro de seguridad JWT)
        |
        +--> auth-ms         :8082  -> db_authms
        +--> productos-ms    :8081  -> db_productosms
        +--> clientes-ms     :8083  -> db_clientesms
        +--> ventas-ms       :8084  -> db_ventasms
        +--> compras-ms      :8085  -> db_comprasms
        +--> proveedores-ms  :8086  -> db_proveedoresms
        +--> despachos-ms    :8087  -> db_despachosms
        +--> vendedores-ms   :8088  -> db_vendedoresms
        +--> inventario-ms   :8089  -> db_inventarioms
        +--> devoluciones-ms :8091  -> db_devolucionesms

Eureka Server :8761
```

---

# 3. Microservicios del sistema

| Módulo | Puerto | Responsabilidad |
| :--- | :---: | :--- |
| `eureka-server` |   8761 | Registro y descubrimiento de microservicios |
| `api-gateway` |   8080 | Enrutador centralizado, balanceo de carga y validación JWT |
| `auth-ms` |   8082 | Seguridad: Registro, login y emisión de Tokens JWT |
| `productos-ms` |   8081 | Catálogo: Productos, marcas y categorías |
| `clientes-ms` |   8083 | Clientes: CRUD y validación de datos tributarios |
| `ventas-ms` |   8084 | Ventas: Emisión de boletas e integración de salida logicial |
| `compras-ms` |   8085 | Compras: Abastecimiento de insumos a bodega |
| `proveedores-ms` |   8086 | Proveedores: CRUD de distribuidores y control fiscal |
| `despachos-ms` |   8087 | Despachos: Generación de guías de despacho y tracking |
| `vendedores-ms` |   8088 | Vendedores: Administración del personal de ventas y comisiones |
| `inventario-ms` |   8089 | Inventario: SKU de bodega, control de stock mínimo e ingresos |
| `devoluciones-ms` |   8091 | Devoluciones: Emisión de Notas de Crédito y reingreso de stock |

---

# 4. Tecnologías utilizadas

* **Java 21**
* **Spring Boot 3.5.14**
* **Spring Cloud (v2025.0.2)**
* **Eureka Discovery Server & Client**
* **Spring Cloud Gateway** (con filtros reactivos)
* **Spring Security & JWT** (Json Web Tokens)
* **OpenFeign** (para llamadas síncronas entre microservicios)
* **Spring Web**
* **Spring Data JPA & Hibernate**
* **MySQL** (Puerto `3307` por defecto)
* **Lombok**
* **JSR-380 Bean Validation**
* **JUnit 5 & Mockito** (Pruebas unitarias de Controller y Service)
* **Springdoc-openapi (Swagger)**
* **Maven** (Arquitectura multi-módulo)

---

# 5. Estructura del proyecto

```text
todogrifos/
├── pom.xml
├── README.md
├── mvnw
├── mvnw.cmd
├── .gitignore
│
├── eureka-server/           # Registro Eureka
├── api-gateway/             # Gateway e Interceptor de seguridad
├── auth-ms/                 # Gestión de accesos
├── productos-ms/            # Catálogo de grifería y marcas
├── clientes-ms/             # Registro de clientes
├── inventario-ms/           # Stock de bodega
├── ventas-ms/               # Transacciones de salida
├── compras-ms/              # Transacciones de entrada
├── proveedores-ms/          # CRUD Proveedores
├── despachos-ms/            # Seguimiento logístico
├── vendedores-ms/           # Comisiones del staff
├── devoluciones-ms/         # Notas de Crédito
│
└── todogrifos-deploy-nativo/ # Carpeta de despliegue local automatizado (.bat)
```

---

# 6. Bases de datos

El proyecto sigue el patrón **Database per Service** utilizando MySQL.

| Microservicio | Base de datos | Tabla principal |
| :--- | :--- | :--- |
| `auth-ms` | `db_authms` | `usuarios` |
| `productos-ms` | `db_productosms` | `productos`, `marcas`, `categorias` |
| `clientes-ms` | `db_clientesms` | `clientes` |
| `ventas-ms` | `db_ventasms` | `ventas`, `venta_detalles` |
| `compras-ms` | `db_comprasms` | `compras`, `compra_detalles` |
| `proveedores-ms` | `db_proveedoresms`| `proveedores` |
| `despachos-ms` | `db_despachosms` | `despachos` |
| `vendedores-ms` | `db_vendedoresms` | `vendedores` |
| `inventario-ms` | `db_inventarioms` | `inventarios` |
| `devoluciones-ms`| `db_devolucionesms`| `devoluciones` |

Cada microservicio incluye datos semilla a través de archivos `data.sql` ejecutados automáticamente en la inicialización de la base de datos.

---

# 7. Configuración de MySQL

El proyecto está configurado para conectarse a MySQL en el puerto **`3307`** (puerto escolar/local por defecto). 

Ejemplo de propiedades de conexión (`application.properties`):
```properties
spring.datasource.url=jdbc:mysql://localhost:3307/db_clientesms?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
spring.sql.init.continue-on-error=true
```

---

# 8. Orden de ejecución

Para levantar los microservicios, primero asegúrate de tener MySQL activo en el puerto `3307` con las bases de datos creadas. El orden de arranque obligatorio es:

| Orden | Servicio | Puerto |
| :---: | :--- | :---: |
| 1 | `eureka-server` | 8761 |
| 2 | *Microservicios de negocio (todos en paralelo)* | 8081-8091 |
| 3 | `api-gateway` | 8080 |

### Arranque Automatizado en Windows:
Puedes arrancar todo el sistema con un solo clic utilizando los scripts disponibles en [todogrifos-deploy-nativo](file:///c:/Users/HP Omen 16/Desktop/DUOC/2026/Desarrollo Fullstack I/todogrifos/todogrifos-deploy-nativo):
1. Ejecuta `compilar-y-preparar.bat` para compilar todo el proyecto y empaquetar los jars.
2. Ejecuta `arrancar-nativo.bat` para levantar Eureka, los servicios y el Gateway con los retardos correctos de inicialización.
3. Ejecuta `detener-nativo.bat` para apagar todos los servicios de golpe y liberar los puertos.

### Arranque Orquestado con Docker Compose:
Si cuentas con **Docker Desktop** iniciado, puedes levantar todo el ecosistema (incluyendo la base de datos MySQL y la creación de todas sus bases internas) en un solo comando sin necesidad de configurar nada localmente:
1. Compila el proyecto completo para generar los archivos `.jar` actualizados:
   ```bash
   ./mvnw clean package -DskipTests
   ```
2. Construye las imágenes y levanta todos los contenedores en segundo plano:
   ```bash
   docker compose up --build -d
   ```
3. Monitorea el inicio de los contenedores usando logs:
   ```bash
   docker compose logs -f
   ```
4. Para detener y remover todos los contenedores y redes creadas:
   ```bash
   docker compose down
   ```


---

# 9. Ejecución desde Terminal

Para levantar cualquier microservicio individualmente desde la terminal integrada:
```bash
cd nombre-del-microservicio
../mvnw spring-boot:run
```

---

# 10. Compilación del proyecto completo

Para limpiar, compilar y empaquetar todos los módulos del proyecto de manera global omitiendo las pruebas unitarias:
```bash
./mvnw clean package -DskipTests
```

---

# 11. Eureka Server

La consola web de Eureka se encuentra activa en:
```text
http://localhost:8761
```
Cuando todos los servicios están en ejecución, deben aparecer registrados en la sección de instancias:
`API-GATEWAY`, `AUTH-MS`, `CLIENTES-MS`, `COMPRAS-MS`, `DESPACHOS-MS`, `DEVOLUCIONES-MS`, `INVENTARIO-MS`, `PRODUCTOS-MS`, `PROVEEDORES-MS`, `VENDEDORES-MS`, `VENTAS-MS`.

---

# 12. API Gateway

El API Gateway es el punto de entrada unificado y escucha en el puerto **`8080`**. Intercepta todas las llamadas (excepto `/api/auth/**`) y valida el Token JWT que se adjunte en la cabecera `Authorization`.

Rutas base expuestas a través del Gateway:
* Autenticación: `http://localhost:8080/api/auth/**`
* Productos: `http://localhost:8080/api/productos/**`
* Clientes: `http://localhost:8080/api/clientes/**`
* Ventas: `http://localhost:8080/api/ventas/**`
* Compras: `http://localhost:8080/api/compras/**`
* Proveedores: `http://localhost:8080/api/proveedores/**`
* Despachos: `http://localhost:8080/api/despachos/**`
* Vendedores: `http://localhost:8080/api/vendedores/**`
* Inventario: `http://localhost:8080/api/inventario/**`
* Devoluciones: `http://localhost:8080/api/devoluciones/**`

---

# 13. Swagger (OpenAPI)

La interfaz Swagger UI está disponible directamente en el puerto local de cada microservicio:

| Microservicio | URL de Swagger UI |
| :--- | :--- |
| `auth-ms` | `http://localhost:8082/swagger-ui/index.html` |
| `productos-ms` | `http://localhost:8081/swagger-ui/index.html` |
| `clientes-ms` | `http://localhost:8083/swagger-ui/index.html` |
| `ventas-ms` | `http://localhost:8084/swagger-ui/index.html` |
| `compras-ms` | `http://localhost:8085/swagger-ui/index.html` |
| `proveedores-ms` | `http://localhost:8086/swagger-ui/index.html` |
| `despachos-ms` | `http://localhost:8087/swagger-ui/index.html` |
| `vendedores-ms` | `http://localhost:8088/swagger-ui/index.html` |
| `inventario-ms` | `http://localhost:8089/swagger-ui/index.html` |
| `devoluciones-ms`| `http://localhost:8091/swagger-ui/index.html` |

---

# 14. Comunicación entre microservicios

El proyecto utiliza **OpenFeign** para la comunicación síncrona entre microservicios:

* **`ventas-ms`** -> llama a **`clientes-ms`** (verifica existencia de cliente) y a **`inventario-ms`** (solicita egreso/descuento de stock).
* **`compras-ms`** -> llama a **`proveedores-ms`** (verifica existencia de distribuidor) y a **`inventario-ms`** (solicita ingreso/aumento de stock).
* **`inventario-ms`** -> llama a **`productos-ms`** (verifica existencia del producto por SKU).
* **`devoluciones-ms`** -> llama a **`ventas-ms`** (verifica que la boleta de origen exista) y a **`inventario-ms`** (reingresa los artículos devueltos).
* **`despachos-ms`** -> llama a **`ventas-ms`** (valida la venta antes de generar la ruta de envío).

---

# 15. Flujo funcional principal

## Paso 1: Registrar un Usuario
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "admin",
  "password": "password123",
  "role": "ROLE_ADMIN"
}
```

## Paso 2: Iniciar Sesión para obtener Token JWT
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password123"
}
```
*Guarda el campo token retornado en la respuesta JSON para usarlo como cabecera `Authorization: Bearer <TOKEN>` en los siguientes pasos.*

## Paso 3: Registrar un Cliente
```http
POST http://localhost:8080/api/clientes
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "rut": "12.345.678-9",
  "nombre": "Distribuidora San Joaquín",
  "email": "contacto@sanjoaquin.cl",
  "telefono": "+56987654321",
  "direccion": "Av. Vicuña Mackenna 4500"
}
```

## Paso 4: Registrar un Producto en Catálogo
```http
POST http://localhost:8080/api/productos
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "sku": "GRF-COC-001",
  "nombre": "Grifo de Cocina Monomando Cromado",
  "descripcion": "Grifo monomando extensible con acabado brillante",
  "precio": 45990,
  "categoriaId": 1,
  "marcaId": 1
}
```

## Paso 5: Inicializar Stock en Inventario
```http
POST http://localhost:8080/api/inventario
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "sku": "GRF-COC-001",
  "stock": 100,
  "stockMinimo": 10,
  "ubicacion": "Bodega Principal Pasillo A"
}
```

## Paso 6: Procesar una Venta (Descuenta stock automáticamente)
```http
POST http://localhost:8080/api/ventas
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "folio": "BOL-2026-0001",
  "clienteId": 1,
  "detalles": [
    {
      "sku": "GRF-COC-001",
      "cantidad": 5,
      "precioUnitario": 45990
    }
  ]
}
```

---

# 16. Validaciones de Datos Implementadas (JSR-380)

### `auth-ms`
* Nombre de usuario y contraseña obligatorios, no vacíos.

### `clientes-ms` & `proveedores-ms`
* RUT obligatorio con formato chileno válido (`regexp = "^[0-9]{1,2}\\.[0-9]{3}\\.[0-9]{3}-[0-9kK]{1}$"`).
* Correo electrónico obligatorio con formato válido.

### `productos-ms`
* SKU obligatorio y único.
* Nombre de producto obligatorio.
* Precio mayor o igual a 0.

### `inventario-ms`
* SKU obligatorio.
* Stock y Stock mínimo obligatorios y no negativos.

### `ventas-ms` & `compras-ms`
* Folio / N° Factura obligatorios y únicos.
* Detalle de artículos no vacío (mínimo 1 artículo por transacción).
* Cantidad y precio de detalles obligatorios y mayores a 0.

---

# 17. Manejo de errores

Cada microservicio gestiona sus excepciones globalmente mediante `@RestControllerAdvice` y `@ExceptionHandler`, retornando códigos HTTP semánticos y detalles claros:

Ejemplo de respuesta al violar una restricción JSR-380:
```json
{
  "timestamp": "2026-06-29T18:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Error de validacion",
  "path": "/api/clientes",
  "errors": {
    "email": "El formato del correo electronico es invalido",
    "rut": "El formato del RUT es invalido. Debe incluir puntos y guion (ej: 12.345.678-9)."
  }
}
```

---

# 18. Logs

Los eventos clave de negocio, inicios de procesos, advertencias de duplicados y llamadas fallidas entre microservicios se registran por consola usando `@Slf4j` de Lombok:

```java
log.info("Iniciando transaccion de venta con folio: {}", dto.getFolio());
log.warn("Intento de registro con RUT duplicado: {}", dto.getRut());
log.error("Fallo distribuido: No se pudo verificar la boleta ID {}", id);
```

---

# 19. Comandos útiles

### Ejecutar todas las pruebas unitarias aisladas (JUnit 5 + Mockito)
```bash
./mvnw test "-Dtest=!*ApplicationTests" "-Dsurefire.failIfNoSpecifiedTests=false"
```
*Este comando corre de forma 100% desconectada y offline todos los tests de controladores y servicios del reactor.*

---

# 20. Documentación adicional

Toda la lógica de scripts y utilidades locales está descrita en:
* [README-EJECUCION-NATIVA.md](file:///c:/Users/HP Omen 16/Desktop/DUOC/2026/Desarrollo Fullstack I/todogrifos/todogrifos-deploy-nativo/README-EJECUCION-NATIVA.md)

---

# 21. Estado actual del proyecto

| Elemento | Estado |
| :--- | :--- |
| Proyecto padre Maven | **Implementado** |
| Registro Eureka Server | **Implementado** |
| API Gateway & Filtros JWT | **Implementado** |
| 10 Microservicios de negocio | **Implementado** |
| Client Feign (Inter-comunicación) | **Implementado** |
| Manejo de errores centralizado | **Implementado** |
| Logs descriptivos (`@Slf4j`) | **Implementado** |
| OpenAPI / Swagger UI local | **Implementado** |
| Cobertura de Pruebas Unitarias (JUnit 5) | **Implementado** |
| Orquestación con Docker Compose | **Implementado** |
| Frontend de usuario (SPA HTML/JS/CSS) | **Implementado** |

---

# 22. Próximas mejoras sugeridas

* Implementación de una interfaz gráfica (Frontend) utilizando React o Thymeleaf.
* Configuración de un servidor de configuración distribuida (Spring Cloud Config).
