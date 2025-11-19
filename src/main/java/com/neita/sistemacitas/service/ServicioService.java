package com.neita.sistemacitas.service;

import com.neita.sistemacitas.dto.ServicioDTO;
import com.neita.sistemacitas.entity.Servicio;
import com.neita.sistemacitas.exception.ResourceNotFoundException;
import com.neita.sistemacitas.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar operaciones relacionadas con servicios.
 * Implementa la lógica de negocio y validaciones necesarias.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ServicioService {

    private final ServicioRepository servicioRepository;

    /**
     * Obtiene todos los servicios del sistema.
     */
    @Transactional(readOnly = true)
    public List<ServicioDTO> obtenerTodos() {
        log.debug("Obteniendo todos los servicios");
        return servicioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un servicio por su ID.
     */
    @Transactional(readOnly = true)
    public ServicioDTO obtenerPorId(Long id) {
        log.debug("Obteniendo servicio con ID: {}", id);
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + id));
        return convertirADTO(servicio);
    }

    /**
     * Obtiene todos los servicios activos.
     */
    @Transactional(readOnly = true)
    public List<ServicioDTO> obtenerActivos() {
        log.debug("Obteniendo servicios activos");
        return servicioRepository.findByActivoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca servicios por nombre.
     */
    @Transactional(readOnly = true)
    public List<ServicioDTO> buscarPorNombre(String nombre) {
        log.debug("Buscando servicios con nombre: {}", nombre);
        return servicioRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene servicios ordenados por precio.
     */
    @Transactional(readOnly = true)
    public List<ServicioDTO> obtenerOrdenadosPorPrecio() {
        log.debug("Obteniendo servicios ordenados por precio");
        return servicioRepository.findAllByOrderByPrecioAsc().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene servicios ordenados por nombre.
     */
    @Transactional(readOnly = true)
    public List<ServicioDTO> obtenerOrdenadosPorNombre() {
        log.debug("Obteniendo servicios ordenados por nombre");
        return servicioRepository.findAllByOrderByNombreAsc().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca servicios por rango de precio.
     */
    @Transactional(readOnly = true)
    public List<ServicioDTO> buscarPorRangoPrecio(Double precioMin, Double precioMax) {
        log.debug("Buscando servicios con precio entre {} y {}", precioMin, precioMax);
        return servicioRepository.findByPrecioBetween(precioMin, precioMax).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Crea un nuevo servicio.
     */
    @Transactional
    public ServicioDTO crear(ServicioDTO servicioDTO) {
        log.info("Creando nuevo servicio: {}", servicioDTO.getNombre());
        
        Servicio servicio = new Servicio();
        servicio.setNombre(servicioDTO.getNombre());
        servicio.setDescripcion(servicioDTO.getDescripcion());
        servicio.setDuracion(servicioDTO.getDuracion());
        servicio.setPrecio(servicioDTO.getPrecio());
        servicio.setActivo(true);

        Servicio guardado = servicioRepository.save(servicio);
        log.info("Servicio creado exitosamente con ID: {}", guardado.getId());
        
        return convertirADTO(guardado);
    }

    /**
     * Actualiza un servicio existente.
     */
    @Transactional
    public ServicioDTO actualizar(Long id, ServicioDTO servicioDTO) {
        log.info("Actualizando servicio con ID: {}", id);
        
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + id));

        servicio.setNombre(servicioDTO.getNombre());
        servicio.setDescripcion(servicioDTO.getDescripcion());
        servicio.setDuracion(servicioDTO.getDuracion());
        servicio.setPrecio(servicioDTO.getPrecio());

        Servicio actualizado = servicioRepository.save(servicio);
        log.info("Servicio actualizado exitosamente con ID: {}", actualizado.getId());
        
        return convertirADTO(actualizado);
    }

    /**
     * Elimina un servicio (eliminación lógica).
     */
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando servicio con ID: {}", id);
        
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + id));
        
        servicio.setActivo(false);
        servicioRepository.save(servicio);
        
        log.info("Servicio eliminado exitosamente con ID: {}", id);
    }

    /**
     * Elimina permanentemente un servicio.
     */
    @Transactional
    public void eliminarPermanente(Long id) {
        log.warn("Eliminando permanentemente servicio con ID: {}", id);
        
        if (!servicioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Servicio no encontrado con ID: " + id);
        }
        
        servicioRepository.deleteById(id);
        log.info("Servicio eliminado permanentemente con ID: {}", id);
    }

    /**
     * Convierte una entidad Servicio a DTO.
     */
    private ServicioDTO convertirADTO(Servicio servicio) {
        ServicioDTO dto = new ServicioDTO();
        dto.setId(servicio.getId());
        dto.setNombre(servicio.getNombre());
        dto.setDescripcion(servicio.getDescripcion());
        dto.setDuracion(servicio.getDuracion());
        dto.setPrecio(servicio.getPrecio());
        dto.setActivo(servicio.getActivo());
        return dto;
    }
}
