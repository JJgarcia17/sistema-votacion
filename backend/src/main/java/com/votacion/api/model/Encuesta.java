package com.votacion.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder; 
import lombok.NoArgsConstructor; 
import lombok.AllArgsConstructor; 
import java.util.List;

@Entity
@Table(name = "encuestas")
@Getter
@Setter
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
public class Encuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    // --- Relación ---
    // Una Encuesta (One) tiene muchas Opciones (Many)
    // "mappedBy" le dice a JPA que la entidad "Opcion" maneja esta relación
    // "cascade" significa que si borramos una encuesta, sus opciones se borran
    // "fetch = FetchType.EAGER" carga las opciones junto con la encuesta
    @OneToMany(mappedBy = "encuesta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Opcion> opciones;
}