
package com.example.proyecto.controladores;

import com.example.proyecto.entidades.Comentario;
import com.example.proyecto.repositorios.ComentarioRepositorio;
import com.example.proyecto.servicios.ComentarioServicio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/comentario")
public class ComentarioControlador {

    @Autowired
    private ComentarioServicio comentarioServicio;

    @Autowired
    ComentarioRepositorio comentarioRepositorio;// Supongamos que tienes un repositorio de comentarios

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable String id) {
        Optional<Comentario> commentOptional = comentarioRepositorio.findById(id);
        if (commentOptional.isPresent()) {
            Comentario comentario = commentOptional.get();
            if (comentarioServicio.containsForbiddenWords(comentario.getContenido())) {
                comentarioRepositorio.delete(comentario);
                return ResponseEntity.ok("Comentario eliminado debido a contenido inapropiado");
            } else {
                return ResponseEntity.badRequest().body("El comentario no contiene contenido inapropiado");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/listar")
    public String listarTodos(ModelMap modelo) {
        List<Comentario> comentarios = comentarioServicio.ListaComentarios();
        modelo.put("comentarios",comentarios);
        return "comentario_list";
    }

}
