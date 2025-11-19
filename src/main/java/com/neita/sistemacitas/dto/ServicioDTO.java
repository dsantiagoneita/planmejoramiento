package com.neita.sistemacitas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferir datos de Servicio entre capas.
 * Incluye validaciones para garantizar la integridad de los datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioDTO {

    private Long id;

    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Size(min = 2, max = 255, message = "El nombre debe tener entre 2 y 255 caracteres")
    private String nombre;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;

    @NotBlank(message = "La duración es obligatoria")
    @Size(max = 255, message = "La duración no puede exceder 255 caracteres")
    private String duracion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    private Double precio;

    private Boolean activo;


}
