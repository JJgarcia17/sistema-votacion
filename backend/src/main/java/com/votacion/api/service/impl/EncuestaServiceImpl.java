package com.votacion.api.service.impl;

import com.votacion.api.dto.EncuestaRequest;
import com.votacion.api.model.Encuesta;
import com.votacion.api.model.Opcion;
import com.votacion.api.repository.EncuestaRepository;
import com.votacion.api.repository.OpcionRepository;
import com.votacion.api.service.EncuestaService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.votacion.api.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EncuestaServiceImpl implements EncuestaService {

    // Inyección de dependencias (Principio SOLID)
    private final EncuestaRepository encuestaRepository;
    private final OpcionRepository opcionRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public List<Encuesta> obtenerTodasLasEncuestas() {
        return encuestaRepository.findAll();
    }

    @Override
    @Transactional
    public Encuesta crearEncuesta(EncuestaRequest encuestaRequest) {

        // --- Patrón GOF: Builder ---
        // Aplicamos el patrón Builder para crear la entidad.
        Encuesta nuevaEncuesta = Encuesta.builder()
                .titulo(encuestaRequest.getTitulo())
                .build();

        // Convertimos los DTOs de Opcion a Entidades Opcion
        List<Opcion> opciones = encuestaRequest.getOpciones().stream()
                .map(opcionDto -> Opcion.builder()
                        .textoOpcion(opcionDto.getTextoOpcion())
                        .encuesta(nuevaEncuesta) // Vinculamos la opción a la encuesta
                        .build())
                .collect(Collectors.toList());

        nuevaEncuesta.setOpciones(opciones); // Asignamos la lista de opciones a la encuesta

        // Guardamos la encuesta. Gracias a "CascadeType.ALL",
        // las opciones se guardarán automáticamente con ella.
        return encuestaRepository.save(nuevaEncuesta);
    }

    @Override
    @Transactional
    public Opcion registrarVoto(Long opcionId) {
        // Intentamos realizar un UPDATE atómico y obtener el nuevo contador (Postgres RETURNING)
        String sql = "UPDATE opciones SET contador_votos = contador_votos + 1 WHERE id = ? RETURNING contador_votos";
        Long nuevoContador;
        try {
            nuevoContador = jdbcTemplate.queryForObject(sql, new Object[]{opcionId}, Long.class);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("Opción no encontrada con id: " + opcionId);
        }

        // Recuperamos la entidad actualizada para devolverla al controlador
        Opcion opcion = opcionRepository.findById(opcionId)
                .orElseThrow(() -> new ResourceNotFoundException("Opción no encontrada con id: " + opcionId));

        opcion.setContadorVotos(nuevoContador);
        return opcion;
    }
}