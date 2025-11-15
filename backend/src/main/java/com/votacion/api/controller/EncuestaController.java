package com.votacion.api.controller;

import com.votacion.api.dto.EncuestaRequest;
import jakarta.validation.Valid;
import com.votacion.api.model.Encuesta;
import com.votacion.api.model.Opcion;
import com.votacion.api.service.EncuestaService; // ¡Importante! Inyectamos la Interfaz
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marca esta clase como un controlador de API REST
@RequestMapping("/api") // Todas las rutas en esta clase empezarán con /api
@RequiredArgsConstructor // Lombok: crea el constructor para la inyección
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier frontend (importante para Docker)
public class EncuestaController {

    // --- Inyección de Dependencias (SOLID) ---
    // Dependemos de la abstracción (Interfaz), no de la implementación.
    private final EncuestaService encuestaService;

    /**
     * Endpoint para OBTENER todas las encuestas.
     * HTTP GET /api/encuestas
     */
    @GetMapping("/encuestas")
    public ResponseEntity<List<Encuesta>> obtenerTodasLasEncuestas() {
        List<Encuesta> encuestas = encuestaService.obtenerTodasLasEncuestas();
        return ResponseEntity.ok(encuestas); // Devuelve 200 OK
    }

    /**
     * Endpoint para CREAR una nueva encuesta.
     * HTTP POST /api/encuestas
     */
    @PostMapping("/encuestas")
    public ResponseEntity<Encuesta> crearEncuesta(@Valid @RequestBody EncuestaRequest encuestaRequest) {
        // @RequestBody convierte el JSON de la petición en nuestro DTO
        Encuesta nuevaEncuesta = encuestaService.crearEncuesta(encuestaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEncuesta); // Devuelve 201 Created
    }

    /**
     * Endpoint para REGISTRAR un voto en una opción.
     * HTTP POST /api/opciones/{id}/votar
     */
    @PostMapping("/opciones/{id}/votar")
    public ResponseEntity<Opcion> registrarVoto(@PathVariable Long id) {
        // @PathVariable toma el {id} de la URL y lo pasa como variable
        Opcion opcionActualizada = encuestaService.registrarVoto(id);
        return ResponseEntity.ok(opcionActualizada); // Devuelve 200 OK
    }
}