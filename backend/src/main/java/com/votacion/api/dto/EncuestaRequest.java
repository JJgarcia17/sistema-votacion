package com.votacion.api.dto;

import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
public class EncuestaRequest {
    // Pedimos el título de la encuesta
    @NotBlank(message = "El título de la encuesta no puede estar vacío")
    private String titulo;
    
    // Y una lista de las opciones que quieren crear
    @Valid
    @Size(min = 2, message = "Se requieren al menos 2 opciones para la encuesta")
    private List<OpcionRequest> opciones;
}