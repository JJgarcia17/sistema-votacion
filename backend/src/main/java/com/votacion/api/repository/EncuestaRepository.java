package com.votacion.api.repository;

import com.votacion.api.model.Encuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// @Repository le dice a Spring que esta es una interfaz de acceso a datos
@Repository
public interface EncuestaRepository extends JpaRepository<Encuesta, Long> {
    // JpaRepository<Encuesta, Long> significa:
    // 1. "Voy a gestionar la entidad 'Encuesta'"
    // 2. "La llave primaria (Id) de 'Encuesta' es de tipo 'Long'"

    // Por ahora, no necesitamos métodos personalizados.
    // JpaRepository ya nos da findAll(), findById(), save(), etc.
}