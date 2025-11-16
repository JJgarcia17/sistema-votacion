package com.votacion.api.service;

import com.votacion.api.dto.EncuestaRequest;
import com.votacion.api.dto.EncuestaResponse;
import com.votacion.api.dto.OpcionResponse;

import java.util.List;

/**
 * Interfaz para la lógica de negocio de las Encuestas.
 * Ahora trabaja con DTOs de respuesta para desacoplar la API de las entidades JPA.
 */
public interface EncuestaService {

    /**
     * Obtiene una lista de todas las encuestas disponibles.
     *
     * @return Una lista de EncuestaResponse (DTOs).
     */
    List<EncuestaResponse> obtenerTodasLasEncuestas();

    /**
     * Crea una nueva encuesta basada en los datos de un DTO.
     *
     * @param encuestaRequest El DTO (Data Transfer Object) con la información.
     * @return La encuesta creada como DTO de respuesta.
     */
    EncuestaResponse crearEncuesta(EncuestaRequest encuestaRequest);

    /**
     * Registra un voto para una opción específica.
     *
     * @param opcionId El ID de la opción que recibe el voto.
     * @return La opción actualizada como DTO de respuesta.
     */
    OpcionResponse registrarVoto(Long opcionId);

}