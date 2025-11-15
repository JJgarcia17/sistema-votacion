package com.votacion.api.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

// @Data de Lombok genera Getters, Setters, toString, equals y hashCode.
// Es un POJO (Plain Old Java Object) perfecto para un DTO.
@Data
public class OpcionRequest {
    // Solo pedimos el texto. No el ID, no el contador de votos.
    @NotBlank(message = "El texto de la opción no puede estar vacío")
    private String textoOpcion;
}