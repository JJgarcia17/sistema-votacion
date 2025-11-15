package com.votacion.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "opciones")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Opcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String textoOpcion;

    // --- ESTA ES LA LÍNEA CORREGIDA ---
    @Builder.Default // <-- AÑADE ESTO
    @Column(name = "contador_votos", columnDefinition = "integer default 0")
    private int contadorVotos = 0;

    // --- Relación ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encuesta_id", nullable = false)
    private Encuesta encuesta;
}