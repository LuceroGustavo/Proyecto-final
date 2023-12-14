package com.example.proyecto.controladores;

import com.example.proyecto.entidades.Rubro;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.servicios.RubroServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author AMD
 */
@Controller
// @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequestMapping("/rubro")
public class RubroControlador {

    @Autowired
    RubroServicio rubroServicio;

    @GetMapping("/registrar")
    public String registrar() {

        return "rubro_form.html";
    }

    //falta //eliminar//
      @PostMapping("/registro")
    public String registro( String nombreRubro, 
                           String descripcion,
                            MultipartFile archivo,
                           ModelMap modelo){
        try {
            rubroServicio.CrearRubro(nombreRubro, descripcion, archivo);
            modelo.put("exito", "El Rubro fue registrado correctamente!");
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "rubro_form.html";
        }
        return "rubro_form.html";        
    }
    
    @GetMapping("/rubros")
public String obtenerRubros(ModelMap modelo) {
    List<Rubro> rubros = rubroServicio.ListaRubros();
    modelo.addAttribute("rubros", rubros);
    return "index1.html";
}

 @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/listar")
public String listarRubro(ModelMap modelo) {
    List<Rubro> rubros = rubroServicio.ListaRubros();
    modelo.addAttribute("rubros", rubros);
    return "rubro_list";
}



    
}
