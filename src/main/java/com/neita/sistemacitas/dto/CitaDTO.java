package com.neita.sistemacitas.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para transferir datos de Cita entre capas.
 * Incluye validaciones para garantizar la integridad de los datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaDTO {

    private Long id;

    @NotNull(message = "La fecha y hora son obligatorias")
    @Future(message = "La fecha y hora deben ser futuras")
    private LocalDateTime fechaHora;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 50, message = "El estado no puede exceder 50 caracteres")
    private String estado;

    @Size(max = 1000, message = "Las notas no pueden exceder 1000 caracteres")
    private String notas;

    private LocalDateTime fechaCreacion;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    private String usuarioNombre;

    @NotNull(message = "El ID del servicio es obligatorio")
    private Long servicioId;

    private String servicioNombre;

    private Double servicioPrecio;

    @NotNull(message = "El ID del profesional es obligatorio")
    private Long profesionalId;

    private String profesionalNombre;

    private String profesionalEspecialidad;


}
