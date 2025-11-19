# Sistema de Citas para Barbería Neita

Sistema profesional de gestión de citas para barbería desarrollado con Spring Boot 3.5.7, que permite administrar usuarios, profesionales, servicios y citas de manera eficiente con un diseño minimalista y cinematográfico.

## Características Principales

- **Arquitectura en capas**: Separación clara entre entidades, DTOs, servicios y controladores
- **Spring Security**: Autenticación y autorización con rol SuperAdmin
- **API REST completa**: Endpoints profesionales con validaciones y manejo de excepciones
- **Interfaz web Thymeleaf**: Diseño moderno, minimalista y cinematográfico
- **JPA/Hibernate**: Persistencia de datos con MySQL
- **Logging profesional**: Trazabilidad completa de operaciones con SLF4J
- **Validaciones**: Validación de datos con Bean Validation
- **Manejo de excepciones**: Sistema centralizado de manejo de errores

## Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.5.7**
  - Spring Data JPA
  - Spring Security
  - Spring Web
  - Spring Validation
- **Thymeleaf** con Thymeleaf Spring Security
- **MySQL 8.0+**
- **Maven 4.0**
- **Lombok** para reducir boilerplate
- **BCrypt** para encriptación de contraseñas

## Modelo de Datos

El sistema gestiona cuatro entidades principales:

### Usuario
- ID (clave primaria)
- Nombre
- Email (único)
- Contraseña (encriptada)
- Teléfono
- Fecha de registro
- Rol (SUPER_ADMIN)
- Estado activo

### Profesional
- ID (clave primaria)
- Especialidad
- Horario disponible
- Estado activo
- Usuario asociado (relación uno a uno)

### Servicio
- ID (clave primaria)
- Nombre
- Descripción
- Duración
- Precio
- Estado activo

### Cita
- ID (clave primaria)
- Fecha y hora
- Estado (PENDIENTE, CONFIRMADA, COMPLETADA, CANCELADA)
- Notas
- Fecha de creación
- Usuario (relación muchos a uno)
- Servicio (relación muchos a uno)
- Profesional (relación muchos a uno)

## Requisitos del Sistema

- **JDK 21** o superior
- **Maven 3.6+**
- **MySQL 8.0+**
- Puerto **8080** disponible

## Configuración de Base de Datos

El sistema se conecta a una base de datos MySQL con las siguientes credenciales configuradas en `application.properties`:

```properties
URL: jdbc:mysql://localhost:3306/Barberia_Neita
Usuario: root
Contraseña: root
```

La base de datos se crea automáticamente si no existe gracias a la configuración `createDatabaseIfNotExist=true`.

## Instalación y Ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/danieloez314-arch/sistema-citas.git
cd sistema-citas
```

### 2. Configurar MySQL

Asegúrate de tener MySQL instalado y ejecutándose en tu máquina local con las credenciales configuradas (root/root). Si deseas usar credenciales diferentes, modifica el archivo `src/main/resources/application.properties`.

### 3. Compilar el proyecto

```bash
mvn clean install
```

### 4. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

O ejecutar el JAR generado:

```bash
java -jar target/sistema-citas-0.0.1-SNAPSHOT.jar
```

### 5. Acceder a la aplicación

Abre tu navegador y accede a:

```
http://localhost:8080
```

## Credenciales por Defecto

Al iniciar la aplicación por primera vez, se crea automáticamente un usuario SuperAdmin:

- **Email**: admin@barberia.com
- **Contraseña**: admin123

**Importante**: Se recomienda cambiar la contraseña después del primer inicio de sesión.

## Estructura del Proyecto

```
src/main/java/com/neita/sistemacitas/
├── config/              # Configuraciones (Security, DataInitializer)
├── controller/          # Controladores REST y MVC
│   ├── HomeController.java
│   ├── UsuarioController.java
│   ├── UsuarioRestController.java
│   ├── ProfesionalController.java
│   ├── ProfesionalRestController.java
│   ├── ServicioController.java
│   ├── ServicioRestController.java
│   ├── CitaController.java
│   └── CitaRestController.java
├── dto/                 # Data Transfer Objects
│   ├── UsuarioDTO.java
│   ├── ProfesionalDTO.java
│   ├── ServicioDTO.java
│   ├── CitaDTO.java
│   ├── ApiResponse.java
│   └── ErrorDetails.java
├── entity/              # Entidades JPA
│   ├── Usuario.java
│   ├── Profesional.java
│   ├── Servicio.java
│   └── Cita.java
├── exception/           # Excepciones personalizadas y manejadores
│   ├── ResourceNotFoundException.java
│   ├── DuplicateResourceException.java
│   └── GlobalExceptionHandler.java
├── repository/          # Repositorios JPA
│   ├── UsuarioRepository.java
│   ├── ProfesionalRepository.java
│   ├── ServicioRepository.java
│   └── CitaRepository.java
├── service/             # Lógica de negocio
│   ├── UsuarioService.java
│   ├── ProfesionalService.java
│   ├── ServicioService.java
│   ├── CitaService.java
│   └── CustomUserDetailsService.java
└── SistemaCitasApplication.java

src/main/resources/
├── static/              # Recursos estáticos
│   ├── css/
│   │   └── style.css
│   └── js/
│       └── main.js
├── templates/           # Plantillas Thymeleaf
│   ├── fragments/
│   │   └── layout.html
│   ├── auth/
│   │   └── login.html
│   ├── usuario/
│   │   ├── lista.html
│   │   └── formulario.html
│   ├── profesional/
│   │   ├── lista.html
│   │   └── formulario.html
│   ├── servicio/
│   │   ├── lista.html
│   │   └── formulario.html
│   ├── cita/
│   │   ├── lista.html
│   │   └── formulario.html
│   └── index.html
└── application.properties
```

## API REST Endpoints

### Usuarios

- `GET /api/usuarios` - Listar todos los usuarios
- `GET /api/usuarios/{id}` - Obtener usuario por ID
- `GET /api/usuarios/activos` - Listar usuarios activos
- `GET /api/usuarios/buscar?nombre={nombre}` - Buscar usuarios por nombre
- `POST /api/usuarios` - Crear nuevo usuario
- `PUT /api/usuarios/{id}` - Actualizar usuario
- `DELETE /api/usuarios/{id}` - Eliminar usuario (lógico)
- `DELETE /api/usuarios/{id}/permanente` - Eliminar usuario permanentemente

### Profesionales

- `GET /api/profesionales` - Listar todos los profesionales
- `GET /api/profesionales/{id}` - Obtener profesional por ID
- `GET /api/profesionales/activos` - Listar profesionales activos
- `GET /api/profesionales/buscar?especialidad={especialidad}` - Buscar por especialidad
- `POST /api/profesionales` - Crear nuevo profesional
- `PUT /api/profesionales/{id}` - Actualizar profesional
- `DELETE /api/profesionales/{id}` - Eliminar profesional (lógico)
- `DELETE /api/profesionales/{id}/permanente` - Eliminar profesional permanentemente

### Servicios

- `GET /api/servicios` - Listar todos los servicios
- `GET /api/servicios/{id}` - Obtener servicio por ID
- `GET /api/servicios/activos` - Listar servicios activos
- `GET /api/servicios/buscar?nombre={nombre}` - Buscar servicios por nombre
- `GET /api/servicios/ordenar/precio` - Listar servicios ordenados por precio
- `GET /api/servicios/ordenar/nombre` - Listar servicios ordenados por nombre
- `POST /api/servicios` - Crear nuevo servicio
- `PUT /api/servicios/{id}` - Actualizar servicio
- `DELETE /api/servicios/{id}` - Eliminar servicio (lógico)
- `DELETE /api/servicios/{id}/permanente` - Eliminar servicio permanentemente

### Citas

- `GET /api/citas` - Listar todas las citas
- `GET /api/citas/{id}` - Obtener cita por ID
- `GET /api/citas/usuario/{usuarioId}` - Listar citas de un usuario
- `GET /api/citas/profesional/{profesionalId}` - Listar citas de un profesional
- `GET /api/citas/servicio/{servicioId}` - Listar citas de un servicio
- `GET /api/citas/estado/{estado}` - Listar citas por estado
- `GET /api/citas/proximas` - Listar próximas citas
- `GET /api/citas/pasadas` - Listar citas pasadas
- `GET /api/citas/rango?inicio={inicio}&fin={fin}` - Listar citas en rango de fechas
- `POST /api/citas` - Crear nueva cita
- `PUT /api/citas/{id}` - Actualizar cita
- `PATCH /api/citas/{id}/estado?estado={estado}` - Cambiar estado de cita
- `DELETE /api/citas/{id}` - Eliminar cita

## Rutas Web (Interfaz Thymeleaf)

- `/` - Página de inicio con estadísticas
- `/login` - Página de inicio de sesión
- `/logout` - Cerrar sesión
- `/usuarios` - Gestión de usuarios
- `/profesionales` - Gestión de profesionales
- `/servicios` - Gestión de servicios
- `/citas` - Gestión de citas

## Diseño y Estilo

El sistema cuenta con un diseño minimalista y cinematográfico que incluye:

- **Paleta de colores oscura**: Fondo negro con acentos dorados
- **Tipografías**: Anton (títulos), Montserrat (cuerpo), Open Sans (texto)
- **Animaciones suaves**: Transiciones y efectos de entrada
- **Diseño responsivo**: Adaptable a diferentes tamaños de pantalla
- **Cards con sombras**: Efecto de profundidad y elevación
- **Botones con gradientes**: Efectos visuales atractivos

## Seguridad

- **Autenticación basada en formularios** con Spring Security
- **Contraseñas encriptadas** con BCrypt
- **Sesiones gestionadas** con límite de una sesión por usuario
- **Protección CSRF** habilitada
- **Rol único SuperAdmin** con acceso completo al sistema

## Logging

El sistema implementa logging profesional con diferentes niveles:

- **DEBUG**: Operaciones detalladas de servicios y repositorios
- **INFO**: Operaciones importantes y exitosas
- **WARN**: Advertencias y situaciones inusuales
- **ERROR**: Errores y excepciones

## Validaciones

Todas las entradas de datos son validadas con:

- Validaciones de Bean Validation (Jakarta Validation)
- Validaciones personalizadas en servicios
- Mensajes de error descriptivos
- Manejo centralizado de excepciones

## Autor

Desarrollado por **Neita** para Barbería Neita

## Repositorio

[https://github.com/danieloez314-arch/sistema-citas](https://github.com/danieloez314-arch/sistema-citas)

## Licencia

Este proyecto es privado y confidencial.

## Notas Adicionales

- El sistema utiliza eliminación lógica para usuarios, profesionales y servicios (campo `activo`)
- Las citas se eliminan físicamente de la base de datos
- Los horarios están configurados para la zona horaria de Colombia (America/Bogota)
- El sistema soporta múltiples estados de citas: PENDIENTE, CONFIRMADA, COMPLETADA, CANCELADA
- Todos los endpoints REST devuelven respuestas en formato JSON con estructura consistente

## Soporte

Para reportar problemas o solicitar nuevas funcionalidades, por favor contacta al equipo de desarrollo.
