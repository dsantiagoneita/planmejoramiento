package com.neita.sistemacitas.repository;

import com.neita.sistemacitas.entity.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Profesional.
 * Proporciona métodos para acceder y manipular datos de profesionales en la base de datos.
 */
@Repository
public interface ProfesionalRepository extends JpaRepository<Profesional, Long> {

    /**
     * Busca todos los profesionales activos.
     * @return lista de profesionales activos
     */
    List<Profesional> findByActivoTrue();

    /**
     * Busca profesionales por especialidad.
     * @param especialidad la especialidad a buscar
     * @return lista de profesionales con esa especialidad
     */
    List<Profesional> findByEspecialidadContainingIgnoreCase(String especialidad);

    /**
     * Busca un profesional por el ID del usuario asociado.
     * @param usuarioId el ID del usuario
     * @return un Optional con el profesional si existe
     */
    Optional<Profesional> findByUsuarioId(Long usuarioId);

    /**
     * Verifica si existe un profesional asociado al usuario especificado.
     * @param usuarioId el ID del usuario
     * @return true si existe, false en caso contrario
     */
    boolean existsByUsuarioId(Long usuarioId);

    /**
     * Cuenta el número de profesionales activos en el sistema.
     * @return el número de profesionales activos
     */
    @Query("SELECT COUNT(p) FROM Profesional p WHERE p.activo = true")
    long countProfesionalesActivos();

    /**
     * Busca profesionales activos por especialidad.
     * @param especialidad la especialidad a buscar
     * @return lista de profesionales activos con esa especialidad
     */
    @Query("SELECT p FROM Profesional p WHERE p.activo = true AND LOWER(p.especialidad) LIKE LOWER(CONCAT('%', :especialidad, '%'))")
    List<Profesional> findProfesionalesActivosByEspecialidad(String especialidad);
}
