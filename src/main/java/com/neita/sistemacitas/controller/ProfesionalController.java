package com.neita.sistemacitas.controller;

import com.neita.sistemacitas.dto.ProfesionalDTO;
import com.neita.sistemacitas.service.ProfesionalService;
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
 * Controlador MVC para gestionar vistas de profesionales.
 */
@Controller
@RequestMapping("/profesionales")
@RequiredArgsConstructor
@Slf4j
public class ProfesionalController {

    private final ProfesionalService profesionalService;
    private final UsuarioService usuarioService;

    /**
     * Lista todos los profesionales.
     */
    @GetMapping
    public String listar(Model model) {
        log.info("Listando todos los profesionales");
        model.addAttribute("title", "Profesionales");
        model.addAttribute("activeMenu", "profesionales");
        model.addAttribute("profesionales", profesionalService.obtenerTodos());
        return "profesional/lista";
    }

    /**
     * Muestra el formulario para crear un nuevo profesional.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        log.info("Mostrando formulario de nuevo profesional");
        model.addAttribute("title", "Nuevo Profesional");
        model.addAttribute("activeMenu", "profesionales");
        model.addAttribute("profesional", new ProfesionalDTO());
        model.addAttribute("usuarios", usuarioService.obtenerActivos());
        model.addAttribute("esNuevo", true);
        return "profesional/formulario";
    }

    /**
     * Muestra el formulario para editar un profesional existente.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        log.info("Mostrando formulario de edición para profesional ID: {}", id);
        model.addAttribute("title", "Editar Profesional");
        model.addAttribute("activeMenu", "profesionales");
        model.addAttribute("profesional", profesionalService.obtenerPorId(id));
        model.addAttribute("usuarios", usuarioService.obtenerActivos());
        model.addAttribute("esNuevo", false);
        return "profesional/formulario";
    }

    /**
     * Guarda un profesional (crear o actualizar).
     */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("profesional") ProfesionalDTO profesionalDTO,
                         BindingResult result,
                         @RequestParam(required = false) Long id,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        
        if (result.hasErrors()) {
            log.warn("Errores de validación al guardar profesional");
            model.addAttribute("title", id == null ? "Nuevo Profesional" : "Editar Profesional");
            model.addAttribute("activeMenu", "profesionales");
            model.addAttribute("usuarios", usuarioService.obtenerActivos());
            model.addAttribute("esNuevo", id == null);
            return "profesional/formulario";
        }

        try {
            if (id == null) {
                profesionalService.crear(profesionalDTO);
                redirectAttributes.addFlashAttribute("mensaje", "Profesional creado exitosamente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            } else {
                profesionalService.actualizar(id, profesionalDTO);
                redirectAttributes.addFlashAttribute("mensaje", "Profesional actualizado exitosamente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            }
            return "redirect:/profesionales";
        } catch (Exception e) {
            log.error("Error al guardar profesional", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al guardar profesional: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/profesionales";
        }
    }

    /**
     * Elimina un profesional (eliminación lógica).
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Eliminando profesional ID: {}", id);
        try {
            profesionalService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Profesional eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            log.error("Error al eliminar profesional", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar profesional: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/profesionales";
    }

    /**
     * Muestra los detalles de un profesional.
     */
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        log.info("Mostrando detalles del profesional ID: {}", id);
        model.addAttribute("title", "Detalle Profesional");
        model.addAttribute("activeMenu", "profesionales");
        model.addAttribute("profesional", profesionalService.obtenerPorId(id));
        return "profesional/detalle";
    }
}
