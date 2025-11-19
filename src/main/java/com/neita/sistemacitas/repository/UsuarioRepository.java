package com.neita.sistemacitas.repository;

import com.neita.sistemacitas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 * Proporciona métodos para acceder y manipular datos de usuarios en la base de datos.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su email.
     * @param email el email del usuario
     * @return un Optional con el usuario si existe
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica si existe un usuario con el email especificado.
     * @param email el email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);

    /**
     * Busca todos los usuarios activos.
     * @return lista de usuarios activos
     */
    List<Usuario> findByActivoTrue();

    /**
     * Busca usuarios por nombre que contenga el texto especificado (búsqueda parcial).
     * @param nombre el texto a buscar en el nombre
     * @return lista de usuarios que coinciden
     */
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca usuarios por rol.
     * @param rol el rol a buscar
     * @return lista de usuarios con ese rol
     */
    List<Usuario> findByRol(String rol);

    /**
     * Cuenta el número de usuarios activos en el sistema.
     * @return el número de usuarios activos
     */
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.activo = true")
    long countUsuariosActivos();
}
