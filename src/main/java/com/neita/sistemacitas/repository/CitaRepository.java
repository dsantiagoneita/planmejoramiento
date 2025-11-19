package com.neita.sistemacitas.repository;

import com.neita.sistemacitas.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad Cita.
 * Proporciona métodos para acceder y manipular datos de citas en la base de datos.
 */
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    /**
     * Busca todas las citas de un usuario específico.
     * @param usuarioId el ID del usuario
     * @return lista de citas del usuario
     */
    List<Cita> findByUsuarioId(Long usuarioId);

    /**
     * Busca todas las citas de un profesional específico.
     * @param profesionalId el ID del profesional
     * @return lista de citas del profesional
     */
    List<Cita> findByProfesionalId(Long profesionalId);

    /**
     * Busca todas las citas de un servicio específico.
     * @param servicioId el ID del servicio
     * @return lista de citas del servicio
     */
    List<Cita> findByServicioId(Long servicioId);

    /**
     * Busca citas por estado.
     * @param estado el estado de la cita
     * @return lista de citas con ese estado
     */
    List<Cita> findByEstado(String estado);

    /**
     * Busca citas entre dos fechas.
     * @param inicio fecha y hora de inicio
     * @param fin fecha y hora de fin
     * @return lista de citas en ese rango de fechas
     */
    List<Cita> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Busca citas de un profesional en un rango de fechas.
     * @param profesionalId el ID del profesional
     * @param inicio fecha y hora de inicio
     * @param fin fecha y hora de fin
     * @return lista de citas del profesional en ese rango
     */
    @Query("SELECT c FROM Cita c WHERE c.profesional.id = :profesionalId AND c.fechaHora BETWEEN :inicio AND :fin")
    List<Cita> findCitasByProfesionalAndFechaHora(
            @Param("profesionalId") Long profesionalId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    /**
     * Busca citas de un usuario ordenadas por fecha descendente.
     * @param usuarioId el ID del usuario
     * @return lista de citas del usuario ordenadas
     */
    List<Cita> findByUsuarioIdOrderByFechaHoraDesc(Long usuarioId);

    /**
     * Busca citas de un profesional ordenadas por fecha ascendente.
     * @param profesionalId el ID del profesional
     * @return lista de citas del profesional ordenadas
     */
    List<Cita> findByProfesionalIdOrderByFechaHoraAsc(Long profesionalId);

    /**
     * Cuenta el número total de citas en el sistema.
     * @return el número total de citas
     */
    @Query("SELECT COUNT(c) FROM Cita c")
    long countTotalCitas();

    /**
     * Cuenta el número de citas por estado.
     * @param estado el estado de la cita
     * @return el número de citas con ese estado
     */
    long countByEstado(String estado);

    /**
     * Busca las próximas citas (futuras) ordenadas por fecha.
     * @param ahora la fecha y hora actual
     * @return lista de citas futuras
     */
    @Query("SELECT c FROM Cita c WHERE c.fechaHora >= :ahora ORDER BY c.fechaHora ASC")
    List<Cita> findProximasCitas(@Param("ahora") LocalDateTime ahora);

    /**
     * Busca citas pasadas ordenadas por fecha descendente.
     * @param ahora la fecha y hora actual
     * @return lista de citas pasadas
     */
    @Query("SELECT c FROM Cita c WHERE c.fechaHora < :ahora ORDER BY c.fechaHora DESC")
    List<Cita> findCitasPasadas(@Param("ahora") LocalDateTime ahora);
}
