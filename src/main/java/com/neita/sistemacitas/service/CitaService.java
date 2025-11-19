package com.neita.sistemacitas.service;

import com.neita.sistemacitas.dto.CitaDTO;
import com.neita.sistemacitas.entity.Cita;
import com.neita.sistemacitas.entity.Profesional;
import com.neita.sistemacitas.entity.Servicio;
import com.neita.sistemacitas.entity.Usuario;
import com.neita.sistemacitas.exception.ResourceNotFoundException;
import com.neita.sistemacitas.repository.CitaRepository;
import com.neita.sistemacitas.repository.ProfesionalRepository;
import com.neita.sistemacitas.repository.ServicioRepository;
import com.neita.sistemacitas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar operaciones relacionadas con citas.
 * Implementa la lógica de negocio y validaciones necesarias.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CitaService {

    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ServicioRepository servicioRepository;
    private final ProfesionalRepository profesionalRepository;

    /**
     * Obtiene todas las citas del sistema.
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> obtenerTodas() {
        log.debug("Obteniendo todas las citas");
        return citaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una cita por su ID.
     */
    @Transactional(readOnly = true)
    public CitaDTO obtenerPorId(Long id) {
        log.debug("Obteniendo cita con ID: {}", id);
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));
        return convertirADTO(cita);
    }

    /**
     * Obtiene todas las citas de un usuario.
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> obtenerPorUsuario(Long usuarioId) {
        log.debug("Obteniendo citas del usuario con ID: {}", usuarioId);
        return citaRepository.findByUsuarioIdOrderByFechaHoraDesc(usuarioId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las citas de un profesional.
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> obtenerPorProfesional(Long profesionalId) {
        log.debug("Obteniendo citas del profesional con ID: {}", profesionalId);
        return citaRepository.findByProfesionalIdOrderByFechaHoraAsc(profesionalId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las citas de un servicio.
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> obtenerPorServicio(Long servicioId) {
        log.debug("Obteniendo citas del servicio con ID: {}", servicioId);
        return citaRepository.findByServicioId(servicioId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene citas por estado.
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> obtenerPorEstado(String estado) {
        log.debug("Obteniendo citas con estado: {}", estado);
        return citaRepository.findByEstado(estado).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene las próximas citas (futuras).
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> obtenerProximas() {
        log.debug("Obteniendo próximas citas");
        return citaRepository.findProximasCitas(LocalDateTime.now()).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene citas pasadas.
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> obtenerPasadas() {
        log.debug("Obteniendo citas pasadas");
        return citaRepository.findCitasPasadas(LocalDateTime.now()).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene citas en un rango de fechas.
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> obtenerPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        log.debug("Obteniendo citas entre {} y {}", inicio, fin);
        return citaRepository.findByFechaHoraBetween(inicio, fin).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Crea una nueva cita.
     */
    @Transactional
    public CitaDTO crear(CitaDTO citaDTO) {
        log.info("Creando nueva cita para usuario ID: {}", citaDTO.getUsuarioId());
        
        // Verificar que existen las entidades relacionadas
        Usuario usuario = usuarioRepository.findById(citaDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + citaDTO.getUsuarioId()));
        
        Servicio servicio = servicioRepository.findById(citaDTO.getServicioId())
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + citaDTO.getServicioId()));
        
        Profesional profesional = profesionalRepository.findById(citaDTO.getProfesionalId())
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con ID: " + citaDTO.getProfesionalId()));

        Cita cita = new Cita();
        cita.setFechaHora(citaDTO.getFechaHora());
        cita.setEstado(citaDTO.getEstado() != null ? citaDTO.getEstado() : "PENDIENTE");
        cita.setNotas(citaDTO.getNotas());
        cita.setFechaCreacion(LocalDateTime.now());
        cita.setUsuario(usuario);
        cita.setServicio(servicio);
        cita.setProfesional(profesional);

        Cita guardada = citaRepository.save(cita);
        log.info("Cita creada exitosamente con ID: {}", guardada.getId());
        
        return convertirADTO(guardada);
    }

    /**
     * Actualiza una cita existente.
     */
    @Transactional
    public CitaDTO actualizar(Long id, CitaDTO citaDTO) {
        log.info("Actualizando cita con ID: {}", id);
        
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));

        // Actualizar servicio si cambió
        if (!cita.getServicio().getId().equals(citaDTO.getServicioId())) {
            Servicio servicio = servicioRepository.findById(citaDTO.getServicioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + citaDTO.getServicioId()));
            cita.setServicio(servicio);
        }

        // Actualizar profesional si cambió
        if (!cita.getProfesional().getId().equals(citaDTO.getProfesionalId())) {
            Profesional profesional = profesionalRepository.findById(citaDTO.getProfesionalId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con ID: " + citaDTO.getProfesionalId()));
            cita.setProfesional(profesional);
        }

        cita.setFechaHora(citaDTO.getFechaHora());
        cita.setEstado(citaDTO.getEstado());
        cita.setNotas(citaDTO.getNotas());

        Cita actualizada = citaRepository.save(cita);
        log.info("Cita actualizada exitosamente con ID: {}", actualizada.getId());
        
        return convertirADTO(actualizada);
    }

    /**
     * Cambia el estado de una cita.
     */
    @Transactional
    public CitaDTO cambiarEstado(Long id, String nuevoEstado) {
        log.info("Cambiando estado de cita ID: {} a {}", id, nuevoEstado);
        
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));
        
        cita.setEstado(nuevoEstado);
        Cita actualizada = citaRepository.save(cita);
        
        log.info("Estado de cita actualizado exitosamente");
        return convertirADTO(actualizada);
    }

    /**
     * Elimina permanentemente una cita.
     */
    @Transactional
    public void eliminar(Long id) {
        log.warn("Eliminando cita con ID: {}", id);
        
        if (!citaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cita no encontrada con ID: " + id);
        }
        
        citaRepository.deleteById(id);
        log.info("Cita eliminada exitosamente con ID: {}", id);
    }

    /**
     * Convierte una entidad Cita a DTO.
     */
    private CitaDTO convertirADTO(Cita cita) {
        CitaDTO dto = new CitaDTO();
        dto.setId(cita.getId());
        dto.setFechaHora(cita.getFechaHora());
        dto.setEstado(cita.getEstado());
        dto.setNotas(cita.getNotas());
        dto.setFechaCreacion(cita.getFechaCreacion());
        dto.setUsuarioId(cita.getUsuario().getId());
        dto.setUsuarioNombre(cita.getUsuario().getNombre());
        dto.setServicioId(cita.getServicio().getId());
        dto.setServicioNombre(cita.getServicio().getNombre());
        dto.setServicioPrecio(cita.getServicio().getPrecio());
        dto.setProfesionalId(cita.getProfesional().getId());
        dto.setProfesionalNombre(cita.getProfesional().getUsuario().getNombre());
        dto.setProfesionalEspecialidad(cita.getProfesional().getEspecialidad());
        return dto;
    }
}
