# JWT Implementation Guide

## Flujo de Autenticación

### 1. Registrar Usuario
```
POST http://localhost:8080/users
Content-Type: application/json

{
  "username": "juan123",
  "password": "password123"
}

Response:
{
  "id": 1,
  "username": "juan123",
  "typeUser": 2
}
```

### 2. Login - Obtener JWT Token
```
POST http://localhost:8080/users/login
Content-Type: application/json

{
  "username": "juan123",
  "password": "password123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": 1,
  "username": "juan123",
  "type": "Bearer"
}
```

### 3. Crear Artículo (Con Token)
```
POST http://localhost:8080/articles
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "title": "Mi Primer Artículo",
  "body": "Contenido del artículo",
  "userId": 1
}

Response:
{
  "id": 1,
  "title": "Mi Primer Artículo",
  "body": "Contenido del artículo",
  "user": {
    "id": 1,
    "username": "juan123",
    "typeUser": 2
  }
}
```

### 4. Actualizar Artículo (Con Token)
```
PUT http://localhost:8080/articles/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "title": "Artículo Actualizado",
  "body": "Contenido actualizado",
  "userId": 1
}

Response:
{
  "id": 1,
  "title": "Artículo Actualizado",
  "body": "Contenido actualizado",
  "user": {
    "id": 1,
    "username": "juan123",
    "typeUser": 2
  }
}
```

### 5. Eliminar Artículo (Con Token)
```
DELETE http://localhost:8080/articles/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

Response: 204 No Content
```

## Flujo Técnico

1. **User registers** → POST /users → Usuario guardado en BD
2. **User logs in** → POST /users/login → JWT Token generado por JwtService
3. **User sends authenticated request** → Authorization: Bearer {token}
4. **JwtAuthenticationFilter intercepts request** → Extrae token del header
5. **Filter valida token** → Verifica firma y expiración
6. **User cargado en SecurityContext** → Disponible en @GetAuthentication
7. **ArticleController extrae userId** → De SecurityContextHolder
8. **ArticleService verifica permisos** → Compara userId del token con propietario

## Variables de Entorno (Importante)

En tu `.env` o configuración debe estar:

```properties
JWT_SECRET_KEY=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

Para generar una clave segura (256 bits):
```bash
openssl rand -base64 32
```

## Endpoints Públicos vs Protegidos

### Públicos (sin autenticación)
- GET /users
- POST /users (register)
- POST /users/login
- GET /articles
- GET /articles/{id}
- GET /articles/user/{userId}

### Protegidos (requieren JWT)
- POST /articles (crear)
- PUT /articles/{id} (actualizar - solo propietario)
- DELETE /articles/{id} (eliminar - solo propietario)

## En Postman

1. **Para requests con JWT:**
   - Ve a la pestaña "Authorization"
   - Selecciona "Bearer Token"
   - Pega el token obtenido del /users/login

2. **O en Headers:**
   - Authorization: Bearer {token}
