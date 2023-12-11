package com.example.proyecto.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.proyecto.entidades.Persona;
import com.example.proyecto.servicios.PersonaServicio;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador { // VERIFICAR!!!   - IMAGEN CONTROLADOR PERSONA (PARA UNIFICAR CLIENTE Y PROVEEDOR Y ADMIN) 
    
    @Autowired
    PersonaServicio personaServicio;
    
    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario (@PathVariable String id){
        Persona persona = personaServicio.getOne(id);
        
       byte[] imagen= persona.getImagen().getContenido();
       
       HttpHeaders headers = new HttpHeaders();
       
       headers.setContentType(MediaType.IMAGE_JPEG);
       
       return new ResponseEntity<>(imagen,headers, HttpStatus.OK); 
    }
}

