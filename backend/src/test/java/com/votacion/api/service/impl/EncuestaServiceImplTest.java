package com.votacion.api.service.impl;

import com.votacion.api.dto.OpcionResponse;
import com.votacion.api.exception.ResourceNotFoundException;
import com.votacion.api.model.Opcion;
import com.votacion.api.repository.EncuestaRepository;
import com.votacion.api.repository.OpcionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
/**
 * Unit tests for {@link com.votacion.api.service.impl.EncuestaServiceImpl}.
 *
 * Objetivo de las pruebas:
 * - Verificar el comportamiento de {@code registrarVoto} en distintos escenarios
 * - Mantener las pruebas unitarias aisladas mediante mocks de `JdbcTemplate`,
 *   `OpcionRepository` y `EncuestaRepository`.
 *
 * Organización:
 * - Los tests unitarios deben residir en el mismo paquete que las clases bajo prueba
 *   (mirroring de `src/main/java`) para facilitar el acceso de paquetes por defecto
 *   y mantener coherencia con la arquitectura.
 */
class EncuestaServiceImplTest {

    @Mock
    private EncuestaRepository encuestaRepository;

    @Mock
    private OpcionRepository opcionRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private EncuestaServiceImpl encuestaService;

    @Test
    void registrarVoto_success() {
        Long opcionId = 1L;
        // Arrange: preparar mocks
        // Simula que el UPDATE atómico con RETURNING devolvió el nuevo contador (7)
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Long.class))).thenReturn(7L);

        // Simulamos la entidad devuelta por el repositorio con el contador actualizado
        Opcion opcion = new Opcion();
        opcion.setId(opcionId);
        opcion.setTextoOpcion("Opción A");
        opcion.setContadorVotos(7);
        when(opcionRepository.findById(opcionId)).thenReturn(Optional.of(opcion));

        // Act: ejecutar la operación bajo prueba
        OpcionResponse resp = encuestaService.registrarVoto(opcionId);

        // Assert: validar el resultado y las interacciones
        assertNotNull(resp);
        assertEquals(7, resp.getContadorVotos());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), eq(Long.class));
        // Nota: esta implementación no usa EntityManager.refresh; no se verifica aquí.
    }

    @Test
    void registrarVoto_whenUpdateAffectsNoRows_thenThrowResourceNotFound() {
        Long opcionId = 2L;
        // Arrange: forzar que la consulta atómica no devuelva filas
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Long.class)))
            .thenThrow(new EmptyResultDataAccessException(1));

        // Act & Assert: debe lanzarse ResourceNotFoundException
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
            () -> encuestaService.registrarVoto(opcionId));

        assertTrue(ex.getMessage().contains("Opción no encontrada"));
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), eq(Long.class));
        verify(opcionRepository, never()).findById(anyLong());
    }
}
