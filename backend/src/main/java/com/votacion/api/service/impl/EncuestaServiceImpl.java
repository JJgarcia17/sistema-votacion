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
        // Intentamos realizar un UPDATE atómico y obtener el nuevo contador (Postgres RETURNING)
        String sql = "UPDATE opciones SET contador_votos = contador_votos + 1 WHERE id = ? RETURNING contador_votos";
        Long nuevoContador;
        try {
            nuevoContador = jdbcTemplate.queryForObject(sql, new Object[]{opcionId}, Long.class);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("Opción no encontrada con id: " + opcionId);
        }

        Opcion opcion = opcionRepository.findById(opcionId)
                .orElseThrow(() -> new ResourceNotFoundException("Opción no encontrada con id: " + opcionId));

        opcion.setContadorVotos(nuevoContador);
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