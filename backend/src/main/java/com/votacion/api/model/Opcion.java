package com.votacion.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Builder.Default
    @Column(name = "contador_votos", columnDefinition = "bigint default 0")
    private long contadorVotos = 0L;

    // --- AÑADE ESTA ANOTACIÓN ---
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encuesta_id", nullable = false)
    private Encuesta encuesta;
}