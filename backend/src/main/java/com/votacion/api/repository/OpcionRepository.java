package com.votacion.api.repository;

import com.votacion.api.model.Opcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpcionRepository extends JpaRepository<Opcion, Long> {
    // "Voy a gestionar la entidad 'Opcion'"
    // "La llave primaria (Id) de 'Opcion' es de tipo 'Long'"

    // Más adelante, usaremos este repositorio para encontrar una opción
    // por su ID y sumarle un voto.
}