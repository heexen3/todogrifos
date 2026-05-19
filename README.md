# TodoGrifos — Arquitectura de Microservicios

**Video explicativo:**
https://www.youtube.com/watch?v=7FGUdbPyFSM

Backend para la gestión integral de una tienda de **grifería y ferretería**.

El sistema cubre:

- Catálogo de productos
- Clientes
- Ventas
- Compras
- Inventario
- Proveedores
- Vendedores
- Despachos
- Devoluciones
- Autenticación y autorización

---

## Integrantes

- **Luis Echevarria**
- **Claudio Bertin**

---

## Tecnologías

### Backend

- Java 21
- Spring Boot 3
- Spring Cloud Netflix Eureka
- Spring Cloud Gateway
- OpenFeign

### Persistencia

- Spring Data JPA
- Hibernate
- MySQL

### Seguridad

- Spring Security
- JWT (JSON Web Tokens)

### Utilidades

- Bean Validation
- Lombok

---

# Arquitectura

El proyecto sigue una arquitectura basada en **microservicios**, compuesta por:

- **2 componentes de infraestructura**
- **10 microservicios de negocio**
- **Database per Service Pattern**

## Componentes

| Componente | Puerto | Tipo | Responsabilidad |
|-------------|--------:|------|------------------|
| `eureka-server` | 8761 | Infraestructura | Registro y descubrimiento de servicios |
| `api-gateway` | 8080 | Infraestructura | Entrada única, routing y validación JWT |
| `auth-ms` | 8082 | Negocio | Registro, login y emisión de tokens |
| `productos-ms` | 8081 | Negocio | Catálogo de productos, marcas y categorías |
| `clientes-ms` | 8083 | Negocio | CRUD de clientes |
| `ventas-ms` | 8084 | Negocio | Registro de ventas y descuento de stock |
| `compras-ms` | 8085 | Negocio | Registro de compras y aumento de stock |
| `proveedores-ms` | 8086 | Negocio | CRUD de proveedores |
| `despachos-ms` | 8087 | Negocio | Órdenes de despacho y tracking |
| `vendedores-ms` | 8088 | Negocio | CRUD de vendedores y comisiones |
| `inventario-ms` | 8089 | Negocio | Control de stock por SKU |
| `devoluciones-ms` | 8091 | Negocio | Notas de crédito y reingreso/merma |

---

## Organización interna de cada microservicio

Cada servicio mantiene su propia base de datos y sigue una estructura similar:

```plaintext
controller/
service/
repository/
model/
dto/
exception/
client/
```

---

# Persistencia

La persistencia se implementa con **JPA/Hibernate**.

Cada servicio utiliza:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Hibernate genera o actualiza automáticamente el esquema usando anotaciones como:

- `@Entity`
- `@Table`
- `@Id`
- `@ManyToOne`
- `@OneToMany`

---

## Datos semilla

No se utiliza **Flyway** ni **Liquibase** en esta versión.

Cada microservicio incluye un archivo `data.sql` con información inicial.

Configuración utilizada:

```properties
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
spring.sql.init.continue-on-error=true
```

Los inserts utilizan:

```sql
INSERT IGNORE
```

Esto evita duplicación de registros al reiniciar los servicios.

---

# Bases de Datos

MySQL debe estar disponible en:

```plaintext
localhost:3307
```

Configuración:

| Parámetro | Valor |
|------------|-------|
| Usuario | root |
| Password | vacío |

## Bases configuradas

- `db_authms`
- `db_productosms`
- `db_clientesms`
- `db_ventasms`
- `db_comprasms`
- `db_proveedoresms`
- `db_despachosms`
- `db_vendedoresms`
- `db_inventarioms`
- `db_devolucionesms`

---

# Ejecución

## Orden de inicio

1. Levantar MySQL en puerto **3307**
2. Ejecutar `eureka-server`
3. Ejecutar `api-gateway`
4. Ejecutar `auth-ms`
5. Ejecutar el resto de microservicios

## Comando de ejecución

Desde la carpeta de cada servicio:

```powershell
.\mvnw.cmd spring-boot:run
```

---

# Seguridad

## Endpoints públicos

### Registro

```http
POST http://localhost:8080/api/auth/register
```

### Login

```http
POST http://localhost:8080/api/auth/login
```

---

## Endpoints protegidos

Todos los demás endpoints requieren JWT:

```http
Authorization: Bearer <token>
```

---

## Usuario de prueba

```json
{
  "username": "demo",
  "password": "password"
}
```

---

# Datos Semilla

| Dato | Valor |
|------|------|
| Usuario | `demo / password` |
| Cliente principal | `ID 1` — `12.345.678-9` |
| Productos | `GRF-COC-001`, `GRF-BAN-002` |
| Inventario | Stock inicial para ambos SKU |
| Venta | `BOL-2026-0001` |
| Compra | `FAC-2026-0001` |
| Despacho | `TRK-2026-0001` |
| Devolución | `NCG-2026-0001` |

---

# Comunicación entre Microservicios

Comunicación síncrona mediante **OpenFeign**.

## Relaciones

### ventas-ms

- valida clientes usando `clientes-ms`
- descuenta stock usando `inventario-ms`

### compras-ms

- aumenta stock mediante `inventario-ms`

### inventario-ms

- valida existencia de SKU usando `productos-ms`

### devoluciones-ms

- valida venta original usando `ventas-ms`
- puede reingresar stock usando `inventario-ms`

---

# Endpoints Principales

| Servicio | Endpoints                                                                                                    |
|----------|--------------------------------------------------------------------------------------------------------------|
| `auth-ms` | `POST /api/auth/register` — `POST /api/auth/login`                                                           |
| `productos-ms` | `GET/POST /api/productos` • `GET/PUT/DELETE /api/productos/{id}` • `GET /api/productos/sku/{sku}`            |
| `clientes-ms` | `GET/POST /api/clientes` • `GET/PUT/DELETE /api/clientes/{id}` • `GET /api/clientes/rut/{rut}`               |
| `ventas-ms` | `GET/POST /api/ventas` • `GET/DELETE /api/ventas/{id}`                                                       |
| `compras-ms` | `GET/POST /api/compras` • `GET/DELETE /api/compras/{id}`                                                     |
| `proveedores-ms` | `GET/POST /api/proveedores` • `GET/PUT/DELETE /api/proveedores/{id}` • `GET /api/proveedores/rut/{rut}`      |
| `despachos-ms` | `GET/POST /api/despachos` • `GET/DELETE /api/despachos/{id}` • `PUT /api/despachos/{id}/estado`              |
| `vendedores-ms` | `GET/POST /api/vendedores` • `GET/PUT/DELETE /api/vendedores/{id}` • `PUT /api/vendedores/{id}/comision`     |
| `inventario-ms` | `GET/POST /api/inventario` • `GET/PUT/DELETE /api/inventario/{sku}` • `PUT /agregar` • `PUT /retirar`        |
| `devoluciones-ms` | `GET/POST /api/devoluciones` • `GET/DELETE /api/devoluciones/{id}` • `GET /api/devoluciones/venta/{ventaId}` |

---

# Patrón Arquitectónico

Este proyecto implementa:

- Microservices Architecture
- API Gateway Pattern
- Service Discovery Pattern
- Database per Service Pattern
- JWT Authentication
- Synchronous Inter-Service Communication (OpenFeign)
