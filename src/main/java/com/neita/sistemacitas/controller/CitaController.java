package com.neita.sistemacitas.controller;

import com.neita.sistemacitas.dto.CitaDTO;
import com.neita.sistemacitas.service.CitaService;
import com.neita.sistemacitas.service.ProfesionalService;
import com.neita.sistemacitas.service.ServicioService;
import com.neita.sistemacitas.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador MVC para gestionar vistas de citas.
 */
@Controller
@RequestMapping("/citas")
@RequiredArgsConstructor
@Slf4j
public class CitaController {

    private final CitaService citaService;
    private final UsuarioService usuarioService;
    private final ServicioService servicioService;
    private final ProfesionalService profesionalService;

    /**
     * Lista todas las citas.
     */
    @GetMapping
    public String listar(Model model) {
        log.info("Listando todas las citas");
        model.addAttribute("title", "Citas");
        model.addAttribute("activeMenu", "citas");
        model.addAttribute("citas", citaService.obtenerTodas());
        return "cita/lista";
    }

    /**
     * Muestra el formulario para crear una nueva cita.
     */
    @GetMapping("/nueva")
    public String mostrarFormularioNuevo(Model model) {
        log.info("Mostrando formulario de nueva cita");
        model.addAttribute("title", "Nueva Cita");
        model.addAttribute("activeMenu", "citas");
        model.addAttribute("cita", new CitaDTO());
        model.addAttribute("usuarios", usuarioService.obtenerActivos());
        model.addAttribute("servicios", servicioService.obtenerActivos());
        model.addAttribute("profesionales", profesionalService.obtenerActivos());
        model.addAttribute("esNuevo", true);
        return "cita/formulario";
    }

    /**
     * Muestra el formulario para editar una cita existente.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        log.info("Mostrando formulario de edición para cita ID: {}", id);
        model.addAttribute("title", "Editar Cita");
        model.addAttribute("activeMenu", "citas");
        model.addAttribute("cita", citaService.obtenerPorId(id));
        model.addAttribute("usuarios", usuarioService.obtenerActivos());
        model.addAttribute("servicios", servicioService.obtenerActivos());
        model.addAttribute("profesionales", profesionalService.obtenerActivos());
        model.addAttribute("esNuevo", false);
        return "cita/formulario";
    }

    /**
     * Guarda una cita (crear o actualizar).
     */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("cita") CitaDTO citaDTO,
                         BindingResult result,
                         @RequestParam(required = false) Long id,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        
        if (result.hasErrors()) {
            log.warn("Errores de validación al guardar cita");
            model.addAttribute("title", id == null ? "Nueva Cita" : "Editar Cita");
            model.addAttribute("activeMenu", "citas");
            model.addAttribute("usuarios", usuarioService.obtenerActivos());
            model.addAttribute("servicios", servicioService.obtenerActivos());
            model.addAttribute("profesionales", profesionalService.obtenerActivos());
            model.addAttribute("esNuevo", id == null);
            return "cita/formulario";
        }

        try {
            if (id == null) {
                citaService.crear(citaDTO);
                redirectAttributes.addFlashAttribute("mensaje", "Cita creada exitosamente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            } else {
                citaService.actualizar(id, citaDTO);
                redirectAttributes.addFlashAttribute("mensaje", "Cita actualizada exitosamente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            }
            return "redirect:/citas";
        } catch (Exception e) {
            log.error("Error al guardar cita", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al guardar cita: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/citas";
        }
    }

    /**
     * Elimina una cita.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Eliminando cita ID: {}", id);
        try {
            citaService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Cita eliminada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            log.error("Error al eliminar cita", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar cita: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/citas";
    }

    /**
     * Muestra los detalles de una cita.
     */
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        log.info("Mostrando detalles de la cita ID: {}", id);
        model.addAttribute("title", "Detalle Cita");
        model.addAttribute("activeMenu", "citas");
        model.addAttribute("cita", citaService.obtenerPorId(id));
        return "cita/detalle";
    }

    /**
     * Cambia el estado de una cita.
     */
    @PostMapping("/cambiar-estado/{id}")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam String estado,
                                RedirectAttributes redirectAttributes) {
        log.info("Cambiando estado de cita ID: {} a {}", id, estado);
        try {
            citaService.cambiarEstado(id, estado);
            redirectAttributes.addFlashAttribute("mensaje", "Estado de cita actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            log.error("Error al cambiar estado de cita", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al cambiar estado: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/citas";
    }
}
