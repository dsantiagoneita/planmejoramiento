package com.neita.sistemacitas.config;

import com.neita.sistemacitas.entity.Usuario;
import com.neita.sistemacitas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Inicializador de datos para crear el usuario SuperAdmin por defecto.
 * Se ejecuta al iniciar la aplicación si no existe ningún usuario.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Verificar si ya existe algún usuario
        if (usuarioRepository.count() == 0) {
            log.info("No se encontraron usuarios. Creando usuario SuperAdmin por defecto...");
            
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setEmail("admin@barberia.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setTelefono("3001234567");
            admin.setFechaRegistro(LocalDateTime.now());
            admin.setRol("SUPER_ADMIN");
            admin.setActivo(true);

            usuarioRepository.save(admin);
            
            log.info("Usuario SuperAdmin creado exitosamente");
            log.info("Email: admin@barberia.com");
            log.info("Contraseña: admin123");
            log.info("Por favor, cambie la contraseña después del primer inicio de sesión");
        } else {
            log.info("Usuarios existentes encontrados. Omitiendo creación de usuario por defecto.");
        }
    }
}
