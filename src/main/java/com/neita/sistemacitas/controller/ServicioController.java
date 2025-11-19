package com.neita.sistemacitas.controller;

import com.neita.sistemacitas.dto.ServicioDTO;
import com.neita.sistemacitas.service.ServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador MVC para gestionar vistas de servicios.
 */
@Controller
@RequestMapping("/servicios")
@RequiredArgsConstructor
@Slf4j
public class ServicioController {

    private final ServicioService servicioService;

    /**
     * Lista todos los servicios.
     */
    @GetMapping
    public String listar(Model model) {
        log.info("Listando todos los servicios");
        model.addAttribute("title", "Servicios");
        model.addAttribute("activeMenu", "servicios");
        model.addAttribute("servicios", servicioService.obtenerTodos());
        return "servicio/lista";
    }

    /**
     * Muestra el formulario para crear un nuevo servicio.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        log.info("Mostrando formulario de nuevo servicio");
        model.addAttribute("title", "Nuevo Servicio");
        model.addAttribute("activeMenu", "servicios");
        model.addAttribute("servicio", new ServicioDTO());
        model.addAttribute("esNuevo", true);
        return "servicio/formulario";
    }

    /**
     * Muestra el formulario para editar un servicio existente.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        log.info("Mostrando formulario de edición para servicio ID: {}", id);
        model.addAttribute("title", "Editar Servicio");
        model.addAttribute("activeMenu", "servicios");
        model.addAttribute("servicio", servicioService.obtenerPorId(id));
        model.addAttribute("esNuevo", false);
        return "servicio/formulario";
    }

    /**
     * Guarda un servicio (crear o actualizar).
     */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("servicio") ServicioDTO servicioDTO,
                         BindingResult result,
                         @RequestParam(required = false) Long id,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        
        if (result.hasErrors()) {
            log.warn("Errores de validación al guardar servicio");
            model.addAttribute("title", id == null ? "Nuevo Servicio" : "Editar Servicio");
            model.addAttribute("activeMenu", "servicios");
            model.addAttribute("esNuevo", id == null);
            return "servicio/formulario";
        }

        try {
            if (id == null) {
                servicioService.crear(servicioDTO);
                redirectAttributes.addFlashAttribute("mensaje", "Servicio creado exitosamente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            } else {
                servicioService.actualizar(id, servicioDTO);
                redirectAttributes.addFlashAttribute("mensaje", "Servicio actualizado exitosamente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            }
            return "redirect:/servicios";
        } catch (Exception e) {
            log.error("Error al guardar servicio", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al guardar servicio: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/servicios";
        }
    }

    /**
     * Elimina un servicio (eliminación lógica).
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Eliminando servicio ID: {}", id);
        try {
            servicioService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Servicio eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            log.error("Error al eliminar servicio", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar servicio: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/servicios";
    }

    /**
     * Muestra los detalles de un servicio.
     */
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        log.info("Mostrando detalles del servicio ID: {}", id);
        model.addAttribute("title", "Detalle Servicio");
        model.addAttribute("activeMenu", "servicios");
        model.addAttribute("servicio", servicioService.obtenerPorId(id));
        return "servicio/detalle";
    }
}
