package com.neita.sistemacitas.controller;

import com.neita.sistemacitas.dto.UsuarioDTO;
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
 * Controlador MVC para gestionar vistas de usuarios.
 */
@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Lista todos los usuarios.
     */
    @GetMapping
    public String listar(Model model) {
        log.info("Listando todos los usuarios");
        model.addAttribute("title", "Usuarios");
        model.addAttribute("activeMenu", "usuarios");
        model.addAttribute("usuarios", usuarioService.obtenerTodos());
        return "usuario/lista";
    }

    /**
     * Muestra el formulario para crear un nuevo usuario.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        log.info("Mostrando formulario de nuevo usuario");
        model.addAttribute("title", "Nuevo Usuario");
        model.addAttribute("activeMenu", "usuarios");
        model.addAttribute("usuario", new UsuarioDTO());
        model.addAttribute("esNuevo", true);
        return "usuario/formulario";
    }

    /**
     * Muestra el formulario para editar un usuario existente.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        log.info("Mostrando formulario de edición para usuario ID: {}", id);
        model.addAttribute("title", "Editar Usuario");
        model.addAttribute("activeMenu", "usuarios");
        model.addAttribute("usuario", usuarioService.obtenerPorId(id));
        model.addAttribute("esNuevo", false);
        return "usuario/formulario";
    }

    /**
     * Guarda un usuario (crear o actualizar).
     */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                         BindingResult result,
                         @RequestParam(required = false) Long id,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        
        if (result.hasErrors()) {
            log.warn("Errores de validación al guardar usuario");
            model.addAttribute("title", id == null ? "Nuevo Usuario" : "Editar Usuario");
            model.addAttribute("activeMenu", "usuarios");
            model.addAttribute("esNuevo", id == null);
            return "usuario/formulario";
        }

        try {
            if (id == null) {
                usuarioService.crear(usuarioDTO);
                redirectAttributes.addFlashAttribute("mensaje", "Usuario creado exitosamente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            } else {
                usuarioService.actualizar(id, usuarioDTO);
                redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            }
            return "redirect:/usuarios";
        } catch (Exception e) {
            log.error("Error al guardar usuario", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al guardar usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/usuarios";
        }
    }

    /**
     * Elimina un usuario (eliminación lógica).
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Eliminando usuario ID: {}", id);
        try {
            usuarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            log.error("Error al eliminar usuario", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/usuarios";
    }

    /**
     * Muestra los detalles de un usuario.
     */
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        log.info("Mostrando detalles del usuario ID: {}", id);
        model.addAttribute("title", "Detalle Usuario");
        model.addAttribute("activeMenu", "usuarios");
        model.addAttribute("usuario", usuarioService.obtenerPorId(id));
        return "usuario/detalle";
    }
}
