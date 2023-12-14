/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.proyecto.controladores;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Admin
 */
@Controller
@RequestMapping("/admin")

public class AdministradorControlador {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dashboard")
    public String panelAdeministracion(ModelMap modelo){
        modelo.addAttribute("isAdmin", true);
        return "dashboard.html";
    }
    
   
}
