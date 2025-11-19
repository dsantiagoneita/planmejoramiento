package com.neita.sistemacitas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un servicio ofrecido por la barbería.
 * Un servicio puede estar asociado a múltiples citas.
 */
@Entity
@Table(name = "servicio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, length = 255)
    private String duracion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Boolean activo = true;

    // Relación uno a muchos con Cita
    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cita> citas = new ArrayList<>();

    /**
     * Método auxiliar para agregar una cita al servicio.
     * Mantiene la consistencia bidireccional de la relación.
     */
    public void agregarCita(Cita cita) {
        citas.add(cita);
        cita.setServicio(this);
    }

    /**
     * Método auxiliar para remover una cita del servicio.
     * Mantiene la consistencia bidireccional de la relación.
     */
    public void removerCita(Cita cita) {
        citas.remove(cita);
        cita.setServicio(null);
    }
}
