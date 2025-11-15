package com.votacion.api.dto;

import lombok.Data;
import java.util.List;

@Data
public class EncuestaRequest {
    // Pedimos el título de la encuesta
    private String titulo;
    
    // Y una lista de las opciones que quieren crear
    private List<OpcionRequest> opciones;
}