package com.neita.sistemacitas.controller;

import com.neita.sistemacitas.dto.ApiResponse;
import com.neita.sistemacitas.dto.ProfesionalDTO;
import com.neita.sistemacitas.service.ProfesionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones de profesionales.
 * Proporciona endpoints para CRUD completo de profesionales.
 */
@RestController
@RequestMapping("/api/profesionales")
@RequiredArgsConstructor
@Slf4j
public class ProfesionalRestController {

    private final ProfesionalService profesionalService;

    /**
     * Obtiene todos los profesionales.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProfesionalDTO>>> obtenerTodos() {
        log.info("GET /api/profesionales - Obteniendo todos los profesionales");
        List<ProfesionalDTO> profesionales = profesionalService.obtenerTodos();
        return ResponseEntity.ok(ApiResponse.success("Profesionales obtenidos exitosamente", profesionales));
    }

    /**
     * Obtiene un profesional por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProfesionalDTO>> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/profesionales/{} - Obteniendo profesional", id);
        ProfesionalDTO profesional = profesionalService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success("Profesional obtenido exitosamente", profesional));
    }

    /**
     * Obtiene todos los profesionales activos.
     */
    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<ProfesionalDTO>>> obtenerActivos() {
        log.info("GET /api/profesionales/activos - Obteniendo profesionales activos");
        List<ProfesionalDTO> profesionales = profesionalService.obtenerActivos();
        return ResponseEntity.ok(ApiResponse.success("Profesionales activos obtenidos exitosamente", profesionales));
    }

    /**
     * Busca profesionales por especialidad.
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<ProfesionalDTO>>> buscarPorEspecialidad(@RequestParam String especialidad) {
        log.info("GET /api/profesionales/buscar?especialidad={} - Buscando profesionales", especialidad);
        List<ProfesionalDTO> profesionales = profesionalService.buscarPorEspecialidad(especialidad);
        return ResponseEntity.ok(ApiResponse.success("Búsqueda completada exitosamente", profesionales));
    }

    /**
     * Crea un nuevo profesional.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProfesionalDTO>> crear(@Valid @RequestBody ProfesionalDTO profesionalDTO) {
        log.info("POST /api/profesionales - Creando nuevo profesional");
        ProfesionalDTO nuevoProfesional = profesionalService.crear(profesionalDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Profesional creado exitosamente", nuevoProfesional));
    }

    /**
     * Actualiza un profesional existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProfesionalDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProfesionalDTO profesionalDTO) {
        log.info("PUT /api/profesionales/{} - Actualizando profesional", id);
        ProfesionalDTO profesionalActualizado = profesionalService.actualizar(id, profesionalDTO);
        return ResponseEntity.ok(ApiResponse.success("Profesional actualizado exitosamente", profesionalActualizado));
    }

    /**
     * Elimina un profesional (eliminación lógica).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/profesionales/{} - Eliminando profesional", id);
        profesionalService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Profesional eliminado exitosamente"));
    }

    /**
     * Elimina permanentemente un profesional.
     */
    @DeleteMapping("/{id}/permanente")
    public ResponseEntity<ApiResponse<Void>> eliminarPermanente(@PathVariable Long id) {
        log.warn("DELETE /api/profesionales/{}/permanente - Eliminando profesional permanentemente", id);
        profesionalService.eliminarPermanente(id);
        return ResponseEntity.ok(ApiResponse.success("Profesional eliminado permanentemente"));
    }
}
