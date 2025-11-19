package com.neita.sistemacitas.controller;

import com.neita.sistemacitas.dto.ApiResponse;
import com.neita.sistemacitas.dto.ServicioDTO;
import com.neita.sistemacitas.service.ServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones de servicios.
 * Proporciona endpoints para CRUD completo de servicios.
 */
@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
@Slf4j
public class ServicioRestController {

    private final ServicioService servicioService;

    /**
     * Obtiene todos los servicios.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ServicioDTO>>> obtenerTodos() {
        log.info("GET /api/servicios - Obteniendo todos los servicios");
        List<ServicioDTO> servicios = servicioService.obtenerTodos();
        return ResponseEntity.ok(ApiResponse.success("Servicios obtenidos exitosamente", servicios));
    }

    /**
     * Obtiene un servicio por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServicioDTO>> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/servicios/{} - Obteniendo servicio", id);
        ServicioDTO servicio = servicioService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success("Servicio obtenido exitosamente", servicio));
    }

    /**
     * Obtiene todos los servicios activos.
     */
    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<ServicioDTO>>> obtenerActivos() {
        log.info("GET /api/servicios/activos - Obteniendo servicios activos");
        List<ServicioDTO> servicios = servicioService.obtenerActivos();
        return ResponseEntity.ok(ApiResponse.success("Servicios activos obtenidos exitosamente", servicios));
    }

    /**
     * Busca servicios por nombre.
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<ServicioDTO>>> buscarPorNombre(@RequestParam String nombre) {
        log.info("GET /api/servicios/buscar?nombre={} - Buscando servicios", nombre);
        List<ServicioDTO> servicios = servicioService.buscarPorNombre(nombre);
        return ResponseEntity.ok(ApiResponse.success("Búsqueda completada exitosamente", servicios));
    }

    /**
     * Obtiene servicios ordenados por precio.
     */
    @GetMapping("/ordenar/precio")
    public ResponseEntity<ApiResponse<List<ServicioDTO>>> obtenerOrdenadosPorPrecio() {
        log.info("GET /api/servicios/ordenar/precio - Obteniendo servicios ordenados por precio");
        List<ServicioDTO> servicios = servicioService.obtenerOrdenadosPorPrecio();
        return ResponseEntity.ok(ApiResponse.success("Servicios ordenados por precio", servicios));
    }

    /**
     * Obtiene servicios ordenados por nombre.
     */
    @GetMapping("/ordenar/nombre")
    public ResponseEntity<ApiResponse<List<ServicioDTO>>> obtenerOrdenadosPorNombre() {
        log.info("GET /api/servicios/ordenar/nombre - Obteniendo servicios ordenados por nombre");
        List<ServicioDTO> servicios = servicioService.obtenerOrdenadosPorNombre();
        return ResponseEntity.ok(ApiResponse.success("Servicios ordenados por nombre", servicios));
    }

    /**
     * Crea un nuevo servicio.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ServicioDTO>> crear(@Valid @RequestBody ServicioDTO servicioDTO) {
        log.info("POST /api/servicios - Creando nuevo servicio");
        ServicioDTO nuevoServicio = servicioService.crear(servicioDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Servicio creado exitosamente", nuevoServicio));
    }

    /**
     * Actualiza un servicio existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ServicioDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ServicioDTO servicioDTO) {
        log.info("PUT /api/servicios/{} - Actualizando servicio", id);
        ServicioDTO servicioActualizado = servicioService.actualizar(id, servicioDTO);
        return ResponseEntity.ok(ApiResponse.success("Servicio actualizado exitosamente", servicioActualizado));
    }

    /**
     * Elimina un servicio (eliminación lógica).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/servicios/{} - Eliminando servicio", id);
        servicioService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Servicio eliminado exitosamente"));
    }

    /**
     * Elimina permanentemente un servicio.
     */
    @DeleteMapping("/{id}/permanente")
    public ResponseEntity<ApiResponse<Void>> eliminarPermanente(@PathVariable Long id) {
        log.warn("DELETE /api/servicios/{}/permanente - Eliminando servicio permanentemente", id);
        servicioService.eliminarPermanente(id);
        return ResponseEntity.ok(ApiResponse.success("Servicio eliminado permanentemente"));
    }
}
