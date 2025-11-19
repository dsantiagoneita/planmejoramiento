package com.neita.sistemacitas.repository;

import com.neita.sistemacitas.entity.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Servicio.
 * Proporciona métodos para acceder y manipular datos de servicios en la base de datos.
 */
@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    /**
     * Busca todos los servicios activos.
     * @return lista de servicios activos
     */
    List<Servicio> findByActivoTrue();

    /**
     * Busca servicios por nombre que contenga el texto especificado (búsqueda parcial).
     * @param nombre el texto a buscar en el nombre
     * @return lista de servicios que coinciden
     */
    List<Servicio> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca servicios activos por nombre.
     * @param nombre el texto a buscar en el nombre
     * @return lista de servicios activos que coinciden
     */
    @Query("SELECT s FROM Servicio s WHERE s.activo = true AND LOWER(s.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Servicio> findServiciosActivosByNombre(String nombre);

    /**
     * Busca servicios con precio menor o igual al especificado.
     * @param precio el precio máximo
     * @return lista de servicios con precio menor o igual
     */
    List<Servicio> findByPrecioLessThanEqual(Double precio);

    /**
     * Busca servicios con precio entre dos valores.
     * @param precioMin el precio mínimo
     * @param precioMax el precio máximo
     * @return lista de servicios en ese rango de precio
     */
    List<Servicio> findByPrecioBetween(Double precioMin, Double precioMax);

    /**
     * Cuenta el número de servicios activos en el sistema.
     * @return el número de servicios activos
     */
    @Query("SELECT COUNT(s) FROM Servicio s WHERE s.activo = true")
    long countServiciosActivos();

    /**
     * Obtiene los servicios ordenados por precio ascendente.
     * @return lista de servicios ordenados por precio
     */
    List<Servicio> findAllByOrderByPrecioAsc();

    /**
     * Obtiene los servicios ordenados por nombre alfabéticamente.
     * @return lista de servicios ordenados por nombre
     */
    List<Servicio> findAllByOrderByNombreAsc();
}
