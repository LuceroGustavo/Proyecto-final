/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.proyecto.controladores;

import com.example.proyecto.entidades.Persona;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;

import com.example.proyecto.entidades.Rubro;
import com.example.proyecto.excepciones.MiException;

import com.example.proyecto.servicios.ClienteServicio;
import com.example.proyecto.servicios.PersonaServicio;
import com.example.proyecto.servicios.RubroServicio;
import com.example.proyecto.servicios.WhatsappServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author victo
 */
@Controller
/* para que spring sepa que es un controlador */
@RequestMapping("/")
/* cuando cargue va al principal */
public class PortalControlador {

    @Autowired
    RubroServicio rubroServicio;
    @Autowired
    PersonaServicio personaServicio;
    @Autowired
    WhatsappServicio whatsappServicio;

    @Autowired
    ClienteServicio clienteServicio;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN','ROLE_PROVEEDOR')")
    @GetMapping("/inicio")
    public String index1() {
        return "index1.html";
    }

    @GetMapping("/login1")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "usuario o contraseña invalidos");
        }
        return "login1.html";
    }

    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) {
        
        List<Rubro> rubros = rubroServicio.ListaRubros();
        modelo.addAttribute("rubros", rubros);

        return "registroDoble.html";
    }

    @GetMapping("/recuperar")
    public String recuperar() {
        return "contrasenaOlvidada.html";
    }

    @PostMapping("/token")
    public String enviarToken(String email, ModelMap modelo) {
        Persona persona = personaServicio.buscarPorEmail(email);
        String respuesta = null;
        try {
            respuesta = personaServicio.recuperarContraseña(email);
            modelo.put("exito", respuesta);
            modelo.put("numero", 1);
            modelo.put("persona", persona);
        } catch (MiException ex) {
            modelo.put("error", respuesta);
            modelo.put("error", ex.getMessage());
        }
        return "contrasenaOlvidada.html";

    }

    @PostMapping("/cambioContraseña")
    public String cambiarContrasena(String email, String token, String password,String password2, ModelMap modelo) {
          Persona persona = personaServicio.buscarPorEmail(email);       
        try {
                    
             personaServicio.actualizarContrasena(email, token, password, password2);
            modelo.put("exito", "contraseña actualizada correctamente, ya puedes iniciar sesion");
            return "login1.html";
        } catch (MiException ex) {   
             modelo.put("persona", persona);
             modelo.put("numero", 1);
              modelo.put("token", token);
            modelo.put("error", ex.getMessage());
            return "contrasenaOlvidada.html";
        }

        

    }

}
