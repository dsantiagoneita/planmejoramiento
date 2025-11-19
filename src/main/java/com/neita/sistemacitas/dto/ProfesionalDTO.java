package com.neita.sistemacitas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para transferir datos de Profesional entre capas.
 * Incluye validaciones para garantizar la integridad de los datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfesionalDTO {

    private Long id;

    @NotBlank(message = "La especialidad es obligatoria")
    @Size(min = 2, max = 255, message = "La especialidad debe tener entre 2 y 255 caracteres")
    private String especialidad;

    private LocalDateTime horarioDisponible;

    private Boolean activo;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    private String usuarioNombre;

    private String usuarioEmail;


}
