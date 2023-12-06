/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.proyecto.controladores;

import com.example.proyecto.excepciones.MiException;

import com.example.proyecto.servicios.RubroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 *
 * @author AMD */
@Controller
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequestMapping("/rubro")
public class RubroControlador {
    
    @Autowired
    RubroServicio rubroServicio;
    
    @GetMapping("/registrar")
    public String registrar(){
    
return "rubro_form.html";
        }
    
      @PostMapping("/registro")
    public String registro(@RequestParam String nombreRubro,String descripcion, ModelMap modelo) throws MiException{
       //faltan excepciones en el servicio
      //  try {
            rubroServicio.CrearRubro(nombreRubro, descripcion);
            modelo.put("exito", "El Rubro fue registrado correctamente!");
          //   modelo.put("exito", "El Cliente fue registrado correctamente!");
            /*
        } catch (MiException ex) {
                      
            modelo.put("error", ex.getMessage());
            return "rubro_form.html";
        }
        */
        return "index.html";        
    }
}
