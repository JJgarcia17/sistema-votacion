package com.votacion.api.dto;

import lombok.Data;

@Data
public class OpcionResponse {
    private Long id;
    private String textoOpcion;
    private long contadorVotos;
}
