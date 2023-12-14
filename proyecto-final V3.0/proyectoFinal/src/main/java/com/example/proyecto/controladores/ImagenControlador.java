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
import com.example.proyecto.entidades.Rubro;
import com.example.proyecto.servicios.PersonaServicio;
import com.example.proyecto.servicios.ProveedorServicio;
import com.example.proyecto.servicios.RubroServicio;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador { // VERIFICAR!!!   - IMAGEN CONTROLADOR PERSONA (PARA UNIFICAR CLIENTE Y PROVEEDOR Y ADMIN) 
    
    @Autowired
    PersonaServicio personaServicio;
        @Autowired
    RubroServicio rubroServicio;
             @Autowired
    ProveedorServicio proveedorServicio;
    
    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario (@PathVariable String id){

     Persona persona = personaServicio.getOne(id);
     
       byte[] imagen= persona.getImagen().getContenido();
       
    HttpHeaders headers = new  HttpHeaders();    
    
    headers.setContentType(MediaType.IMAGE_JPEG);    
    
    String mime = persona.getImagen().getMime().toLowerCase(); // Asegúrate de que el MIME esté en minúsculas

    MediaType tipo = MediaType.parseMediaType("image/" + mime.replace("/", "_"));
    
    headers.setContentType(tipo);
             
    return new ResponseEntity<> (imagen, headers, HttpStatus.OK);
    }
        /*
        Persona persona = personaServicio.getOne(id);
        
       byte[] imagen= persona.getImagen().getContenido();
       
       HttpHeaders headers = new HttpHeaders();
       
       headers.setContentType(MediaType.IMAGE_JPEG);
       
       return new ResponseEntity<>(imagen,headers, HttpStatus.OK); 
       */
  
       @GetMapping("/rubro/{id}")
      public ResponseEntity<byte[]> imagenRubro (@PathVariable String id){

Rubro rubro = rubroServicio.getOne(id);
          
       byte[] imagen= rubro.getImagen().getContenido();
       
     HttpHeaders headers = new HttpHeaders();
       
       headers.setContentType(MediaType.IMAGE_JPEG);
       
       return new ResponseEntity<>(imagen,headers, HttpStatus.OK); 
      }
      
      /*
      
       @GetMapping("/proveedor/{id}")
      public ResponseEntity<byte[]> imagenProveedor (@PathVariable String id){

Proveedor proveedor = proveedorServicios.
           System.out.println(rubro.getDescripcion());
       byte[] imagen= rubro.getImagen().getContenido();
       
     HttpHeaders headers = new HttpHeaders();
       
       headers.setContentType(MediaType.IMAGE_JPEG);
       
       return new ResponseEntity<>(imagen,headers, HttpStatus.OK); 
      }
      */ 
      
}

