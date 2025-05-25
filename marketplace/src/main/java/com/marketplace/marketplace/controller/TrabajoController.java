package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.model.Trabajo;
import com.marketplace.marketplace.repository.TrabajoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class TrabajoController {

    @Autowired
    private TrabajoRepository trabajoRepository;

    // Página principal (solo trabajos aprobados)
    @GetMapping("/")
    public String mostrarTrabajos(Model model) {
        List<Trabajo> trabajos = trabajoRepository.findByAprobadoTrue();
        model.addAttribute("trabajos", trabajos);
        return "formulario"; // o index, según tu diseño
    }

    // Registro de trabajo (no aprobado por defecto)
    @PostMapping("/registrar-trabajo")
    public String registrarTrabajo(@ModelAttribute Trabajo trabajo, RedirectAttributes redirectAttributes) {
        trabajo.setAprobado(false);
        trabajoRepository.save(trabajo);
        redirectAttributes.addFlashAttribute("mensaje", "Tu trabajo fue registrado y está pendiente de aprobación.");
        return "redirect:/";
    }

    // Categorías públicas (solo aprobados)
    @GetMapping("/diseno-grafico")
    public String mostrarDisenoGrafico(Model model) {
        model.addAttribute("trabajos", trabajoRepository.findByCategoria("Diseño Gráfico")
                .stream().filter(Trabajo::isAprobado).toList());
        return "diseno-grafico";
    }

    @GetMapping("/index")
    public String mostrarIndex(Model model) {
        model.addAttribute("trabajos", trabajoRepository.findByCategoria("Pagina Principal")
                .stream().filter(Trabajo::isAprobado).toList());
        return "index";
    }

    @GetMapping("/desarrollo-web")
    public String mostrarDesarrolloWeb(Model model) {
        model.addAttribute("trabajos", trabajoRepository.findByCategoria("Desarrollo Web")
                .stream().filter(Trabajo::isAprobado).toList());
        return "desarrollo-web";
    }

    @GetMapping("/ilustraciones")
    public String mostrarIlustraciones(Model model) {
        model.addAttribute("trabajos", trabajoRepository.findByCategoria("Ilustraciones")
                .stream().filter(Trabajo::isAprobado).toList());
        return "ilustraciones";
    }

    // Detalle de trabajo
    @GetMapping("/trabajo/{id}")
    public String verDetalleTrabajo(@PathVariable Long id, Model model) {
        Optional<Trabajo> trabajoOptional = trabajoRepository.findById(id);
        if (trabajoOptional.isPresent()) {
            model.addAttribute("trabajo", trabajoOptional.get());
            return "detalle-trabajo";
        } else {
            return "redirect:/";
        }
    }

    // Vista login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    // =================== ADMIN ===================

    // Listar todos los trabajos (admin)
    @GetMapping("/admin/trabajos")
    public String listarTrabajosAdmin(@RequestParam(required = false) String estado, Model model) {
        List<Trabajo> trabajos;

        if ("aprobados".equalsIgnoreCase(estado)) {
            trabajos = trabajoRepository.findByAprobadoTrue();
        } else if ("pendientes".equalsIgnoreCase(estado)) {
            trabajos = trabajoRepository.findByAprobadoFalse();
        } else {
            trabajos = trabajoRepository.findAll();
        }

        model.addAttribute("trabajos", trabajos);
        model.addAttribute("estadoSeleccionado", estado); // útil si quieres mantener el filtro seleccionado en el HTML

        return "admin-trabajos";
    }

    // Cambiar estado aprobado/desaprobado
    @PostMapping("/admin/trabajos/cambiarEstado/{id}")
    public String cambiarEstadoTrabajo(@PathVariable Long id) {
        trabajoRepository.findById(id).ifPresent(trabajo -> {
            trabajo.setAprobado(!trabajo.isAprobado());
            trabajoRepository.save(trabajo);
        });
        return "redirect:/admin/trabajos";
    }

    // Eliminar trabajo
    @GetMapping("/admin/trabajos/eliminar/{id}")
    public String eliminarTrabajo(@PathVariable Long id) {
        trabajoRepository.deleteById(id);
        return "redirect:/admin/trabajos";
    }

    // Editar trabajo (GET)
    @GetMapping("/admin/trabajos/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Trabajo trabajo = trabajoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trabajo no encontrado: " + id));
        model.addAttribute("trabajo", trabajo);
        return "editar-trabajo";
    }

    // Editar trabajo (POST)
    @PostMapping("/admin/trabajos/editar/{id}")
    public String guardarCambios(
            @PathVariable Long id,
            @RequestParam String titulo,
            @RequestParam String descripcion,
            @RequestParam String categoria,
            @RequestParam double precio,
            @RequestParam String imagenUrl,
            @RequestParam(required = false) boolean aprobado) {

        Trabajo trabajo = trabajoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trabajo no encontrado: " + id));

        trabajo.setTitulo(titulo);
        trabajo.setDescripcion(descripcion);
        trabajo.setCategoria(categoria);
        trabajo.setPrecio(precio);
        trabajo.setImagenUrl(imagenUrl);
        trabajo.setAprobado(aprobado);

        trabajoRepository.save(trabajo);
        return "redirect:/admin/trabajos";
    }

    // ============ Recursos estáticos (uploads) ============

    @Configuration
    public static class MvcConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations("file:uploads/");
        }
    }
    // Vista de productos (todos los trabajos)
@GetMapping("/productos")
public String mostrarTodosProductos(Model model) {
    List<Trabajo> trabajosAprobados = trabajoRepository.findByAprobadoTrue();
    model.addAttribute("trabajos", trabajosAprobados);
    return "productos"; // nombre de tu plantilla Thymeleaf
}


}
