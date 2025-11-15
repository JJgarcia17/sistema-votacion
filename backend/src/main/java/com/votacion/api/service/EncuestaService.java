package com.votacion.api.service;

import com.votacion.api.dto.EncuestaRequest;
import com.votacion.api.model.Encuesta;
import com.votacion.api.model.Opcion;

import java.util.List;

/**
 * Interfaz para la lógica de negocio de las Encuestas.
 * Define el "qué" (qué se puede hacer) sin especificar el "cómo".
 * Esto cumple con el Principio de Inversión de Dependencias (SOLID).
 */
public interface EncuestaService {

    /**
     * Obtiene una lista de todas las encuestas disponibles.
     *
     * @return Una lista de entidades Encuesta (incluyendo sus opciones).
     */
    List<Encuesta> obtenerTodasLasEncuestas();

    /**
     * Crea una nueva encuesta basada en los datos de un DTO.
     *
     * @param encuestaRequest El DTO (Data Transfer Object) con la información.
     * @return La entidad Encuesta que fue creada y guardada.
     */
    Encuesta crearEncuesta(EncuestaRequest encuestaRequest);

    /**
     * Registra un voto para una opción específica.
     *
     * @param opcionId El ID de la opción que recibe el voto.
     * @return La entidad Opcion actualizada con el nuevo conteo de votos.
     */
    Opcion registrarVoto(Long opcionId);

}