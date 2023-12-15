/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.proyecto.controladores;

import com.example.proyecto.entidades.Cliente;
import com.example.proyecto.entidades.Comentario;
import com.example.proyecto.entidades.Persona;
import com.example.proyecto.entidades.Trabajo;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.servicios.ClienteServicio;
import com.example.proyecto.servicios.ComentarioServicio;
import com.example.proyecto.servicios.PersonaServicio;
import com.example.proyecto.servicios.TrabajoServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Admin
 */
@Controller
@RequestMapping("/admin")

public class AdministradorControlador {
    
    @Autowired
    ClienteServicio clienteServicio;
    
    @Autowired
    PersonaServicio personaServicio;
    
     @Autowired
    TrabajoServicio trabajoServicio;
    
      @Autowired
    ComentarioServicio comentarioServicio;
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dashboard")
    public String panelAdeministracion(ModelMap modelo){
        modelo.addAttribute("isAdmin", true);
        return "dashboard.html";
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/hacerAdmin/{dni}")
    public String listartodos(@PathVariable String dni, ModelMap modelo) {
        try {
            List<Persona> personas = personaServicio.listarPersonas();
            modelo.put("personas", personas);
            personaServicio.hacerAdmin(dni);
            modelo.put("exito", "la persona fue Hecha Administrador correctamente!");
            return "Lista_personas.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
           List<Persona> personas = personaServicio.listarPersonas();
            modelo.put("personas", personas);
            return "Lista_personas.html";
        }

    }
    
 @GetMapping("/listartodos")
    public String listarTodos(ModelMap modelo, HttpSession session) {
        List<Trabajo> trabajos = trabajoServicio.listarTodos();
        modelo.addAttribute("trabajos", trabajos);

        return "trabajos_list.html";
    }
    
    @GetMapping("/listarFinalizados")
    public String listarFinalizados(ModelMap modelo, HttpSession session) {
      List<Trabajo> trabajos = trabajoServicio.listarFinalizados();
      modelo.addAttribute("finalizados", trabajos);
      
      return "trabajos_list.html";
    
    }   
   @GetMapping("/listarRechazados")
    public String listarRechazados(ModelMap modelo, HttpSession session) {
      List<Trabajo> trabajos = trabajoServicio.listarRechazados();
      modelo.addAttribute("rechazados", trabajos);
      
      return "trabajos_list.htmal";
    
    }
    
    @GetMapping("/listarAceptados")
    public String listarAceptados (ModelMap modelo, HttpSession session) {
      List<Trabajo> trabajos = trabajoServicio.listarAceptados();
      modelo.addAttribute("aceptados", trabajos);
      
      return "trabajos_list.htmal";
    
    }
    
    @GetMapping("/listarMayor") 
   public String listarPorCalificacionMayor(ModelMap modelo) {
       List<Comentario> comentarios = comentarioServicio.ListaComentariosOrdenadosPorCalificacionMayor();
       modelo.put("comentarios", comentarios);
       return "comentario_list.html";
   }
   
    @GetMapping("/listarMenor")
    public String listarPorCalificacionMenor(ModelMap modelo) {
        List<Comentario> comentarios = comentarioServicio.ListaComentariosOrdenadosPorCalificacion();
        modelo.put("comentarios", comentarios);
        return "comentario_list.html";

    }
    
   @GetMapping("/listarFecha")
    public String listarPorFecha(ModelMap modelo) {
        List<Comentario> comentarios = comentarioServicio.ListaComentariosOrdenadosPorFecha();
        modelo.put("comentarios", comentarios);
        return "comentario_list.html";
    } 
}
