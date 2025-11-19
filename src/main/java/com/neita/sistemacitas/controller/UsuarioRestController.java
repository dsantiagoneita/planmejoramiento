package com.neita.sistemacitas.controller;

import com.neita.sistemacitas.dto.ApiResponse;
import com.neita.sistemacitas.dto.UsuarioDTO;
import com.neita.sistemacitas.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones de usuarios.
 * Proporciona endpoints para CRUD completo de usuarios.
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Slf4j
public class UsuarioRestController {

    private final UsuarioService usuarioService;

    /**
     * Obtiene todos los usuarios.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> obtenerTodos() {
        log.info("GET /api/usuarios - Obteniendo todos los usuarios");
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodos();
        return ResponseEntity.ok(ApiResponse.success("Usuarios obtenidos exitosamente", usuarios));
    }

    /**
     * Obtiene un usuario por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/usuarios/{} - Obteniendo usuario", id);
        UsuarioDTO usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success("Usuario obtenido exitosamente", usuario));
    }

    /**
     * Obtiene todos los usuarios activos.
     */
    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> obtenerActivos() {
        log.info("GET /api/usuarios/activos - Obteniendo usuarios activos");
        List<UsuarioDTO> usuarios = usuarioService.obtenerActivos();
        return ResponseEntity.ok(ApiResponse.success("Usuarios activos obtenidos exitosamente", usuarios));
    }

    /**
     * Busca usuarios por nombre.
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> buscarPorNombre(@RequestParam String nombre) {
        log.info("GET /api/usuarios/buscar?nombre={} - Buscando usuarios", nombre);
        List<UsuarioDTO> usuarios = usuarioService.buscarPorNombre(nombre);
        return ResponseEntity.ok(ApiResponse.success("Búsqueda completada exitosamente", usuarios));
    }

    /**
     * Crea un nuevo usuario.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioDTO>> crear(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        log.info("POST /api/usuarios - Creando nuevo usuario");
        UsuarioDTO nuevoUsuario = usuarioService.crear(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Usuario creado exitosamente", nuevoUsuario));
    }

    /**
     * Actualiza un usuario existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO usuarioDTO) {
        log.info("PUT /api/usuarios/{} - Actualizando usuario", id);
        UsuarioDTO usuarioActualizado = usuarioService.actualizar(id, usuarioDTO);
        return ResponseEntity.ok(ApiResponse.success("Usuario actualizado exitosamente", usuarioActualizado));
    }

    /**
     * Elimina un usuario (eliminación lógica).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/usuarios/{} - Eliminando usuario", id);
        usuarioService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Usuario eliminado exitosamente"));
    }

    /**
     * Elimina permanentemente un usuario.
     */
    @DeleteMapping("/{id}/permanente")
    public ResponseEntity<ApiResponse<Void>> eliminarPermanente(@PathVariable Long id) {
        log.warn("DELETE /api/usuarios/{}/permanente - Eliminando usuario permanentemente", id);
        usuarioService.eliminarPermanente(id);
        return ResponseEntity.ok(ApiResponse.success("Usuario eliminado permanentemente"));
    }
}
