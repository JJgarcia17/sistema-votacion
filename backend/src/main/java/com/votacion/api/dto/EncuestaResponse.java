package com.votacion.api.dto;

import lombok.Data;
import java.util.List;

@Data
public class EncuestaResponse {
    private Long id;
    private String titulo;
    private List<OpcionResponse> opciones;
}
