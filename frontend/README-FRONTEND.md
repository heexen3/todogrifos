# 🖥️ Frontend — TodoGrifos Panel de Gestión

## DSY1103 — Desarrollo FullStack I

---

## 1. Descripción

Este directorio contiene el **frontend web** del sistema TodoGrifos. Es una Single Page Application (SPA) construida con HTML, CSS y JavaScript puro que se conecta en tiempo real al **API Gateway** del proyecto a través de peticiones HTTP con autenticación JWT.

### Características
- 🔐 Login con JWT (conectado al `auth-ms`)
- 📊 Dashboard con métricas en tiempo real (cuenta registros de cada microservicio)
- 📋 Visualización de datos de los 9 módulos: Clientes, Productos, Inventario, Ventas, Compras, Proveedores, Vendedores, Despachos y Devoluciones
- 🌙 Diseño oscuro moderno con glassmorphism y animaciones
- ✅ Sin dependencias externas — no requiere `npm install`

---

## 2. Cómo usar — Demo en video (modo local)

### Prerequisitos
- El stack Docker debe estar levantado (`docker compose up --build -d`)
- El API Gateway debe estar corriendo en `http://localhost:8080`

### Opciones para abrir el frontend

#### Opción A: Abrir directamente en el navegador
1. Abre el archivo `frontend/index.html` directamente en Chrome o Edge
2. Inicia sesión con las credenciales de tu usuario en la base de datos

#### Opción B: Servidor local con VS Code Live Server (recomendado para CORS)
1. Instala la extensión **Live Server** en VS Code
2. Clic derecho en `frontend/index.html` → **"Open with Live Server"**
3. El frontend estará disponible en `http://127.0.0.1:5500`

#### Opción C: Servidor HTTP con Python
```bash
cd frontend
python -m http.server 5500
# Abrir: http://localhost:5500
```

---

## 3. Deploy en Vercel

> ⚠️ **Importante**: El frontend en Vercel se conecta a `http://localhost:8080` por defecto.
> Esto significa que solo funciona si el usuario final tiene el backend corriendo localmente.
> Para una demo pública, deberías tener el backend desplegado en una IP o dominio público.

### Pasos para desplegar en Vercel

1. **Instala Vercel CLI** (si no lo tienes):
   ```bash
   npm install -g vercel
   ```

2. **Despliega la carpeta `frontend/`**:
   ```bash
   cd frontend
   vercel
   ```

3. Sigue las instrucciones en pantalla:
   - `Set up and deploy?` → Yes
   - `Which scope?` → Tu cuenta
   - `Link to existing project?` → No
   - `Project name` → `todogrifos-frontend`
   - `Directory` → `./` (estás dentro de `/frontend`)

4. Vercel te entregará una URL pública como `https://todogrifos-frontend.vercel.app`

---

## 4. Flujo de autenticación

```
Browser → POST /api/auth/login → API Gateway → auth-ms → JWT Token
Browser → GET  /api/clientes   → API Gateway (valida JWT) → clientes-ms → JSON
```

El token JWT se almacena en memoria (no en localStorage) y se envía en cada petición como cabecera `Authorization: Bearer <token>`.

---

## 5. Estructura del proyecto

```
frontend/
├── index.html      # Estructura HTML de la SPA
├── styles.css      # Diseño visual completo (dark mode, glassmorphism)
├── app.js          # Lógica de autenticación y consumo de APIs
└── vercel.json     # Configuración para deploy estático en Vercel
```

---

## 6. Credenciales de prueba

Las credenciales dependen de los datos semilla cargados en `auth-ms`.

| Usuario | Contraseña     | Rol        |
|---------|----------------|------------|
| `demo`  | `password123`  | `ROLE_USER` |
| `admin` | `admin123`     | `ROLE_ADMIN`|

> Si no tienes usuarios registrados, usa el endpoint `POST /api/auth/register` para crear uno primero.
