package com.neita.sistemacitas.service;

import com.neita.sistemacitas.dto.UsuarioDTO;
import com.neita.sistemacitas.entity.Usuario;
import com.neita.sistemacitas.exception.ResourceNotFoundException;
import com.neita.sistemacitas.exception.DuplicateResourceException;
import com.neita.sistemacitas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar operaciones relacionadas con usuarios.
 * Implementa la lógica de negocio y validaciones necesarias.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Obtiene todos los usuarios del sistema.
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerTodos() {
        log.debug("Obteniendo todos los usuarios");
        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un usuario por su ID.
     */
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorId(Long id) {
        log.debug("Obteniendo usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return convertirADTO(usuario);
    }

    /**
     * Obtiene un usuario por su email.
     */
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorEmail(String email) {
        log.debug("Obteniendo usuario con email: {}", email);
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        return convertirADTO(usuario);
    }

    /**
     * Obtiene todos los usuarios activos.
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerActivos() {
        log.debug("Obteniendo usuarios activos");
        return usuarioRepository.findByActivoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca usuarios por nombre.
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarPorNombre(String nombre) {
        log.debug("Buscando usuarios con nombre: {}", nombre);
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Crea un nuevo usuario.
     */
    @Transactional
    public UsuarioDTO crear(UsuarioDTO usuarioDTO) {
        log.info("Creando nuevo usuario con email: {}", usuarioDTO.getEmail());
        
        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con el email: " + usuarioDTO.getEmail());
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setRol("SUPER_ADMIN");
        usuario.setActivo(true);

        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario creado exitosamente con ID: {}", guardado.getId());
        
        return convertirADTO(guardado);
    }

    /**
     * Actualiza un usuario existente.
     */
    @Transactional
    public UsuarioDTO actualizar(Long id, UsuarioDTO usuarioDTO) {
        log.info("Actualizando usuario con ID: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        // Verificar si el email ya existe en otro usuario
        if (!usuario.getEmail().equals(usuarioDTO.getEmail()) && 
            usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con el email: " + usuarioDTO.getEmail());
        }

        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setTelefono(usuarioDTO.getTelefono());
        
        // Solo actualizar contraseña si se proporciona una nueva
        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        log.info("Usuario actualizado exitosamente con ID: {}", actualizado.getId());
        
        return convertirADTO(actualizado);
    }

    /**
     * Elimina un usuario (eliminación lógica).
     */
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando usuario con ID: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        
        log.info("Usuario eliminado exitosamente con ID: {}", id);
    }

    /**
     * Elimina permanentemente un usuario.
     */
    @Transactional
    public void eliminarPermanente(Long id) {
        log.warn("Eliminando permanentemente usuario con ID: {}", id);
        
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
        }
        
        usuarioRepository.deleteById(id);
        log.info("Usuario eliminado permanentemente con ID: {}", id);
    }

    /**
     * Convierte una entidad Usuario a DTO.
     */
    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setFechaRegistro(usuario.getFechaRegistro());
        dto.setRol(usuario.getRol());
        dto.setActivo(usuario.getActivo());
        return dto;
    }
}
