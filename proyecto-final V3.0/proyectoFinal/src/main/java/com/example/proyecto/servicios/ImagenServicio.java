package com.example.proyecto.servicios;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.example.proyecto.entidades.Imagen;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.repositorios.ImagenRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



//para ver entre todos

@Service
public class ImagenServicio {
     @Autowired
    private ImagenRepositorio imagenRepositorio;
   
    @Transactional
    public Imagen guardar(MultipartFile archivo) throws MiException{
System.out.println("entrro a la imagen linea 20");

        if (archivo != null && !archivo.isEmpty()) {
            try {
                
                Imagen imagen = new Imagen();
                
                imagen.setMime(archivo.getContentType());
                
                imagen.setNombre(archivo.getName());
                
                imagen.setContenido(archivo.getBytes());
                
                return imagenRepositorio.save(imagen);
                
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.out.println("no guardo la imagen linea 36");
            }
        }
        return null;
    }
       @Transactional
    public Imagen actualizar(MultipartFile archivo, String idImagen) throws MiException{
        if (archivo != null) {
           try {
               
               Imagen imagen = new Imagen();
               
               if (idImagen != null) {
                   Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);
                   
                   if (respuesta.isPresent()) {
                       imagen = respuesta.get();
                   }
               }
               
               imagen.setMime(archivo.getContentType());
               
               imagen.setNombre(archivo.getName());
               
               imagen.setContenido(archivo.getBytes());
               
               return imagenRepositorio.save(imagen);
               
           } catch (Exception e) {
               System.err.println(e.getMessage());
           }
       }
       return null;
       
   }
    @Transactional
    public void eliminar(String idImagen) throws MiException {
        if (idImagen != null) {
            Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);

            if (respuesta.isPresent()) {
                Imagen imagen = respuesta.get();
                imagenRepositorio.delete(imagen);
            } else {
                throw new MiException("No se encontró la imagen " + idImagen);
            }
        } else {
            throw new MiException("El ID de la imagen no puede ser nulo");
          
        }
       
    }

}
