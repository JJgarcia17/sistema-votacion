
# Sistema de Votación — Instrucciones y Estado (Prueba Técnica)

**Overview**

- **Objetivo**: Implementar un sistema simple de encuestas (crear, listar, votar) con Backend en Java, Frontend SPA en Javascript, y despliegue reproducible mediante Docker y Docker Compose.

**Tech Stack**

- **Backend**: Java 17, Spring Boot, Maven, Spring Data JPA, JdbcTemplate, Lombok
- **Frontend**: Vue 3, Vite, Axios
- **Base de Datos**: PostgreSQL (imagen `postgres:14-alpine` en `docker-compose.yml`)
- **Contenerización**: Docker, Docker Compose
- **Testing / CI**: Ninguno integrado aún (véase sección "Próximos pasos")

**Estado de implementación (resumen)**

- **API REST (Backend)**: Implementada — rutas principales:
	- `GET /api/encuestas` — lista todas las encuestas
	- `POST /api/encuestas` — crea una nueva encuesta
	- `POST /api/opciones/{id}/votar` — registra un voto en una opción
- **Frontend (SPA)**: Implementado en `frontend/` (Vue). Consume la API y actualiza la UI sin recargar.
- **Base de datos**: Modelos `Encuesta` y `Opcion` implementados en `backend/src/main/java/com/votacion/api/model`
- **Docker & docker-compose**: `backend/Dockerfile`, `frontend/Dockerfile` y `docker-compose.yml` presentes y configurados para orquestar `db`, `api-servicio` y `ui-servicio`.

**Detalles de implementación importantes**

- **Consistencia de votos**: El incremento de votos se realiza de forma atómica con una sentencia SQL directa usando `JdbcTemplate`:

	- SQL: `UPDATE opciones SET contador_votos = contador_votos + 1 WHERE id = ?`
	- Tras el `UPDATE` se recupera la entidad JPA y se llama a `entityManager.refresh(opcion)` para sincronizar el estado en memoria.
	- El método está anotado con `@Transactional` — este enfoque evita condiciones de carrera en escenarios concurrentes y es eficiente.

- **Variables de entorno**: La configuración de conexión a la BD no está hardcodeada. `application.properties` usa variables de entorno:

	- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASS`

	Estas variables se inyectan desde `docker-compose.yml` al servicio `api-servicio`.

- **Builds optimizados**: Ambos `Dockerfile` (backend y frontend) usan multi-stage builds para producir imágenes más ligeras.

**Estructura clave del repositorio**

- `docker-compose.yml` — orquesta `db`, `api-servicio`, `ui-servicio`
- `backend/` — código Java (Spring Boot)
	- `backend/src/main/java/com/votacion/api/controller/EncuestaController.java`
	- `backend/src/main/java/com/votacion/api/service/impl/EncuestaServiceImpl.java`
	- `backend/src/main/resources/application.properties`
	- `backend/pom.xml`
- `frontend/` — Vue 3 SPA
	- `frontend/src/App.vue`, `frontend/src/components/EncuestaCard.vue`, `frontend/src/components/CrearEncuestaForm.vue`
	- `frontend/src/services/apiService.js`

**Cómo ejecutar (Docker Compose — recomendado)**

1. Prerrequisitos: instalar `docker` y `docker-compose`.
2. En la raíz del proyecto ejecutar:

```pwsh
docker-compose up --build
```

3. Acceder a la aplicación frontend en: `http://localhost:3000`
	 - Backend API base: `http://localhost:8080/api`
	 - Health check (backend): `GET http://localhost:8080/api/health`

**Ejecutar solo el backend localmente (sin Docker)**

```pwsh
cd backend
.\mvnw.cmd package   # Windows
# o en Unix/macOS: ./mvnw package
java -jar target/*.jar
```

**Variables de entorno usadas (resumen)**

- En `docker-compose.yml` (servicio `api-servicio`): `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASS`.
- En `ui-servicio` (frontend): `VITE_API_BASE_URL` (apunta a `http://api-servicio:8080/api` dentro de la red Docker).

**Tests y calidad de código**

- Estado actual: No hay tests automatizados en `backend/src/test`.
- Recomendación: agregar pruebas unitarias para `EncuestaServiceImpl` (mocks para repositorios) y pruebas de integración con H2 o Testcontainers.

**Entregables requeridos por la prueba**

- Código fuente completo (en este repo).
- `Dockerfile` para backend y frontend (presentes).
- `docker-compose.yml` para orquestación (presente).
- `README.md` con instrucciones (este archivo).

