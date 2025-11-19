package com.neita.sistemacitas.service;

import com.neita.sistemacitas.dto.ProfesionalDTO;
import com.neita.sistemacitas.entity.Profesional;
import com.neita.sistemacitas.entity.Usuario;
import com.neita.sistemacitas.exception.ResourceNotFoundException;
import com.neita.sistemacitas.exception.DuplicateResourceException;
import com.neita.sistemacitas.repository.ProfesionalRepository;
import com.neita.sistemacitas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar operaciones relacionadas con profesionales.
 * Implementa la lógica de negocio y validaciones necesarias.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProfesionalService {

    private final ProfesionalRepository profesionalRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Obtiene todos los profesionales del sistema.
     */
    @Transactional(readOnly = true)
    public List<ProfesionalDTO> obtenerTodos() {
        log.debug("Obteniendo todos los profesionales");
        return profesionalRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un profesional por su ID.
     */
    @Transactional(readOnly = true)
    public ProfesionalDTO obtenerPorId(Long id) {
        log.debug("Obteniendo profesional con ID: {}", id);
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con ID: " + id));
        return convertirADTO(profesional);
    }

    /**
     * Obtiene todos los profesionales activos.
     */
    @Transactional(readOnly = true)
    public List<ProfesionalDTO> obtenerActivos() {
        log.debug("Obteniendo profesionales activos");
        return profesionalRepository.findByActivoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca profesionales por especialidad.
     */
    @Transactional(readOnly = true)
    public List<ProfesionalDTO> buscarPorEspecialidad(String especialidad) {
        log.debug("Buscando profesionales con especialidad: {}", especialidad);
        return profesionalRepository.findByEspecialidadContainingIgnoreCase(especialidad).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un profesional por el ID del usuario asociado.
     */
    @Transactional(readOnly = true)
    public ProfesionalDTO obtenerPorUsuarioId(Long usuarioId) {
        log.debug("Obteniendo profesional del usuario con ID: {}", usuarioId);
        Profesional profesional = profesionalRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró profesional para el usuario con ID: " + usuarioId));
        return convertirADTO(profesional);
    }

    /**
     * Crea un nuevo profesional.
     */
    @Transactional
    public ProfesionalDTO crear(ProfesionalDTO profesionalDTO) {
        log.info("Creando nuevo profesional para usuario ID: {}", profesionalDTO.getUsuarioId());
        
        // Verificar que el usuario existe
        Usuario usuario = usuarioRepository.findById(profesionalDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + profesionalDTO.getUsuarioId()));

        // Verificar que el usuario no tenga ya un profesional asociado
        if (profesionalRepository.existsByUsuarioId(profesionalDTO.getUsuarioId())) {
            throw new DuplicateResourceException("El usuario ya tiene un perfil de profesional asociado");
        }

        Profesional profesional = new Profesional();
        profesional.setEspecialidad(profesionalDTO.getEspecialidad());
        profesional.setHorarioDisponible(profesionalDTO.getHorarioDisponible());
        profesional.setActivo(true);
        profesional.setUsuario(usuario);

        Profesional guardado = profesionalRepository.save(profesional);
        log.info("Profesional creado exitosamente con ID: {}", guardado.getId());
        
        return convertirADTO(guardado);
    }

    /**
     * Actualiza un profesional existente.
     */
    @Transactional
    public ProfesionalDTO actualizar(Long id, ProfesionalDTO profesionalDTO) {
        log.info("Actualizando profesional con ID: {}", id);
        
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con ID: " + id));

        profesional.setEspecialidad(profesionalDTO.getEspecialidad());
        profesional.setHorarioDisponible(profesionalDTO.getHorarioDisponible());

        Profesional actualizado = profesionalRepository.save(profesional);
        log.info("Profesional actualizado exitosamente con ID: {}", actualizado.getId());
        
        return convertirADTO(actualizado);
    }

    /**
     * Elimina un profesional (eliminación lógica).
     */
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando profesional con ID: {}", id);
        
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con ID: " + id));
        
        profesional.setActivo(false);
        profesionalRepository.save(profesional);
        
        log.info("Profesional eliminado exitosamente con ID: {}", id);
    }

    /**
     * Elimina permanentemente un profesional.
     */
    @Transactional
    public void eliminarPermanente(Long id) {
        log.warn("Eliminando permanentemente profesional con ID: {}", id);
        
        if (!profesionalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Profesional no encontrado con ID: " + id);
        }
        
        profesionalRepository.deleteById(id);
        log.info("Profesional eliminado permanentemente con ID: {}", id);
    }

    /**
     * Convierte una entidad Profesional a DTO.
     */
    private ProfesionalDTO convertirADTO(Profesional profesional) {
        ProfesionalDTO dto = new ProfesionalDTO();
        dto.setId(profesional.getId());
        dto.setEspecialidad(profesional.getEspecialidad());
        dto.setHorarioDisponible(profesional.getHorarioDisponible());
        dto.setActivo(profesional.getActivo());
        dto.setUsuarioId(profesional.getUsuario().getId());
        dto.setUsuarioNombre(profesional.getUsuario().getNombre());
        dto.setUsuarioEmail(profesional.getUsuario().getEmail());
        return dto;
    }
}
