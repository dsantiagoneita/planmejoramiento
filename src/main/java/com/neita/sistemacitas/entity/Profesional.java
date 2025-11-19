package com.neita.sistemacitas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un profesional en el sistema.
 * Cada profesional está asociado a un usuario y puede gestionar múltiples citas.
 */
@Entity
@Table(name = "profesional")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profesional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String especialidad;

    @Column(name = "horario_disponible")
    private LocalDateTime horarioDisponible;

    @Column(nullable = false)
    private Boolean activo = true;

    // Relación uno a uno con Usuario
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    // Relación uno a muchos con Cita
    @OneToMany(mappedBy = "profesional", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cita> citas = new ArrayList<>();

    /**
     * Método auxiliar para agregar una cita al profesional.
     * Mantiene la consistencia bidireccional de la relación.
     */
    public void agregarCita(Cita cita) {
        citas.add(cita);
        cita.setProfesional(this);
    }

    /**
     * Método auxiliar para remover una cita del profesional.
     * Mantiene la consistencia bidireccional de la relación.
     */
    public void removerCita(Cita cita) {
        citas.remove(cita);
        cita.setProfesional(null);
    }
}
