package com.votacion.api.service.impl;

import com.votacion.api.dto.EncuestaRequest;
import com.votacion.api.dto.EncuestaResponse;
import com.votacion.api.dto.OpcionResponse;
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EncuestaServiceImpl implements EncuestaService {

    // Inyección de dependencias (Principio SOLID)
    private final EncuestaRepository encuestaRepository;
    private final OpcionRepository opcionRepository;
    private final JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<EncuestaResponse> obtenerTodasLasEncuestas() {
        return encuestaRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EncuestaResponse crearEncuesta(EncuestaRequest encuestaRequest) {

        Encuesta nuevaEncuesta = Encuesta.builder()
                .titulo(encuestaRequest.getTitulo())
                .build();

        List<Opcion> opciones = encuestaRequest.getOpciones().stream()
                .map(opcionDto -> Opcion.builder()
                        .textoOpcion(opcionDto.getTextoOpcion())
                        .encuesta(nuevaEncuesta)
                        .build())
                .collect(Collectors.toList());

        nuevaEncuesta.setOpciones(opciones);

        Encuesta guardada = encuestaRepository.save(nuevaEncuesta);
        return toDto(guardada);
    }

    @Override
    @Transactional
    public OpcionResponse registrarVoto(Long opcionId) {
        // Validación básica de entrada: rechazamos ids no positivos
        if (opcionId == null || opcionId <= 0) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "Id de opción inválido");
        }

        // Ejecutamos un UPDATE atómico en la base de datos.
        String sql = "UPDATE opciones SET contador_votos = contador_votos + 1 WHERE id = ?";
        int rows = jdbcTemplate.update(sql, opcionId);
        if (rows == 0) {
            throw new ResourceNotFoundException("Opción no encontrada con id: " + opcionId);
        }

        // Recuperamos la entidad gestionada por JPA y la refrescamos para sincronizar su estado
        // con la base de datos después del UPDATE realizado por JDBC.
        Opcion opcion = opcionRepository.findById(opcionId)
                .orElseThrow(() -> new ResourceNotFoundException("Opción no encontrada con id: " + opcionId));

        // entityManager.refresh garantiza que la entidad refleja los valores actuales de la BD
        entityManager.refresh(opcion);

        return toDto(opcion);
    }

    // Mapeadores a DTOs
    private EncuestaResponse toDto(Encuesta e) {
        EncuestaResponse r = new EncuestaResponse();
        r.setId(e.getId());
        r.setTitulo(e.getTitulo());
        if (e.getOpciones() != null) {
            List<OpcionResponse> opciones = e.getOpciones().stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
            r.setOpciones(opciones);
        }
        return r;
    }

    private OpcionResponse toDto(Opcion o) {
        OpcionResponse r = new OpcionResponse();
        r.setId(o.getId());
        r.setTextoOpcion(o.getTextoOpcion());
        r.setContadorVotos(o.getContadorVotos());
        return r;
    }
}