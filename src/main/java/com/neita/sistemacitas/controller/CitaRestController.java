package com.neita.sistemacitas.controller;

import com.neita.sistemacitas.dto.ApiResponse;
import com.neita.sistemacitas.dto.CitaDTO;
import com.neita.sistemacitas.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para gestionar operaciones de citas.
 * Proporciona endpoints para CRUD completo de citas.
 */
@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@Slf4j
public class CitaRestController {

    private final CitaService citaService;

    /**
     * Obtiene todas las citas.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CitaDTO>>> obtenerTodas() {
        log.info("GET /api/citas - Obteniendo todas las citas");
        List<CitaDTO> citas = citaService.obtenerTodas();
        return ResponseEntity.ok(ApiResponse.success("Citas obtenidas exitosamente", citas));
    }

    /**
     * Obtiene una cita por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CitaDTO>> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/citas/{} - Obteniendo cita", id);
        CitaDTO cita = citaService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success("Cita obtenida exitosamente", cita));
    }

    /**
     * Obtiene todas las citas de un usuario.
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<List<CitaDTO>>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        log.info("GET /api/citas/usuario/{} - Obteniendo citas del usuario", usuarioId);
        List<CitaDTO> citas = citaService.obtenerPorUsuario(usuarioId);
        return ResponseEntity.ok(ApiResponse.success("Citas del usuario obtenidas exitosamente", citas));
    }

    /**
     * Obtiene todas las citas de un profesional.
     */
    @GetMapping("/profesional/{profesionalId}")
    public ResponseEntity<ApiResponse<List<CitaDTO>>> obtenerPorProfesional(@PathVariable Long profesionalId) {
        log.info("GET /api/citas/profesional/{} - Obteniendo citas del profesional", profesionalId);
        List<CitaDTO> citas = citaService.obtenerPorProfesional(profesionalId);
        return ResponseEntity.ok(ApiResponse.success("Citas del profesional obtenidas exitosamente", citas));
    }

    /**
     * Obtiene todas las citas de un servicio.
     */
    @GetMapping("/servicio/{servicioId}")
    public ResponseEntity<ApiResponse<List<CitaDTO>>> obtenerPorServicio(@PathVariable Long servicioId) {
        log.info("GET /api/citas/servicio/{} - Obteniendo citas del servicio", servicioId);
        List<CitaDTO> citas = citaService.obtenerPorServicio(servicioId);
        return ResponseEntity.ok(ApiResponse.success("Citas del servicio obtenidas exitosamente", citas));
    }

    /**
     * Obtiene citas por estado.
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<CitaDTO>>> obtenerPorEstado(@PathVariable String estado) {
        log.info("GET /api/citas/estado/{} - Obteniendo citas por estado", estado);
        List<CitaDTO> citas = citaService.obtenerPorEstado(estado);
        return ResponseEntity.ok(ApiResponse.success("Citas obtenidas exitosamente", citas));
    }

    /**
     * Obtiene las próximas citas (futuras).
     */
    @GetMapping("/proximas")
    public ResponseEntity<ApiResponse<List<CitaDTO>>> obtenerProximas() {
        log.info("GET /api/citas/proximas - Obteniendo próximas citas");
        List<CitaDTO> citas = citaService.obtenerProximas();
        return ResponseEntity.ok(ApiResponse.success("Próximas citas obtenidas exitosamente", citas));
    }

    /**
     * Obtiene citas pasadas.
     */
    @GetMapping("/pasadas")
    public ResponseEntity<ApiResponse<List<CitaDTO>>> obtenerPasadas() {
        log.info("GET /api/citas/pasadas - Obteniendo citas pasadas");
        List<CitaDTO> citas = citaService.obtenerPasadas();
        return ResponseEntity.ok(ApiResponse.success("Citas pasadas obtenidas exitosamente", citas));
    }

    /**
     * Obtiene citas en un rango de fechas.
     */
    @GetMapping("/rango")
    public ResponseEntity<ApiResponse<List<CitaDTO>>> obtenerPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        log.info("GET /api/citas/rango - Obteniendo citas entre {} y {}", inicio, fin);
        List<CitaDTO> citas = citaService.obtenerPorRangoFechas(inicio, fin);
        return ResponseEntity.ok(ApiResponse.success("Citas obtenidas exitosamente", citas));
    }

    /**
     * Crea una nueva cita.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CitaDTO>> crear(@Valid @RequestBody CitaDTO citaDTO) {
        log.info("POST /api/citas - Creando nueva cita");
        CitaDTO nuevaCita = citaService.crear(citaDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Cita creada exitosamente", nuevaCita));
    }

    /**
     * Actualiza una cita existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CitaDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CitaDTO citaDTO) {
        log.info("PUT /api/citas/{} - Actualizando cita", id);
        CitaDTO citaActualizada = citaService.actualizar(id, citaDTO);
        return ResponseEntity.ok(ApiResponse.success("Cita actualizada exitosamente", citaActualizada));
    }

    /**
     * Cambia el estado de una cita.
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<CitaDTO>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        log.info("PATCH /api/citas/{}/estado - Cambiando estado a {}", id, estado);
        CitaDTO citaActualizada = citaService.cambiarEstado(id, estado);
        return ResponseEntity.ok(ApiResponse.success("Estado de cita actualizado exitosamente", citaActualizada));
    }

    /**
     * Elimina una cita.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/citas/{} - Eliminando cita", id);
        citaService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Cita eliminada exitosamente"));
    }
}
