package com.votacion.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.HashMap;

// @RestController le dice a Spring que esta clase es un controlador
// y que los métodos devolverán JSON (o texto plano).
@RestController
// @RequestMapping mapea todas las peticiones bajo "/api" a este controlador.
@RequestMapping("/api")
public class HealthController {

    // @GetMapping mapea peticiones GET a "/health"
    // Resultado: se accederá a este método en GET /api/health
    @GetMapping("/health")
    public Map<String, String> checkHealth() {
        // Devuelve un JSON simple para saber que la API está viva.
        // Usamos un Map para que Spring lo convierta a {"status": "OK"}
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        return response;
    }
}