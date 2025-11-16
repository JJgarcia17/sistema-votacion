package com.votacion.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Smoke test: carga mínima del contexto de Spring Boot.
 *
 * Propósito:
 * - Verificar que la aplicación se inicia correctamente en el entorno de pruebas
 * - Usa una base de datos en memoria (H2) para evitar dependencias externas
 *
 * Ubicación:
 * - Este test vive en `src/test/java/com/votacion/api` y comparte paquete con la clase
 *   principal para que SpringBoot la detecte fácilmente.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
class ApiApplicationTests {

	/**
	 * Test vacío que valida la carga del ApplicationContext.
	 * Buenas prácticas:
	 * - Mantenerlo ligero (usa H2) para no depender de contenedores externos
	 * - No realizar aserciones aquí; su objetivo es fallar rápido si el contexto no arranca
	 */
	@Test
	void contextLoads() {
	}

}
