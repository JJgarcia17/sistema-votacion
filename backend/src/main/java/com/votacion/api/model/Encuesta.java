package com.votacion.api.model;


import com.fasterxml.jackson.annotation.JsonManagedReference; 
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

    // --- AÑADE ESTA ANOTACIÓN ---
    @JsonManagedReference
    @OneToMany(mappedBy = "encuesta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Opcion> opciones;
}