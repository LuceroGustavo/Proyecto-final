/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.proyecto.servicios;
import com.example.proyecto.entidades.Comentario;
import com.example.proyecto.entidades.Trabajo;
import com.example.proyecto.enumeraciones.EstadoTrabajo;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.repositorios.ComentarioRepositorio;
import com.example.proyecto.repositorios.TrabajoRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

//para ver entre todos


@Service
public class ComentarioServicio {

    @Autowired
    ComentarioRepositorio comentarioRepositorio;

    @Autowired
    TrabajoRepositorio trabajoRepositorio;

    
    /*
    ANTES QUE NADA MUY BIEN CHICOS!!!!!!!!!!!!!!
    AQUI VAN UNAS PEQUEÑAS SUGERENCIA
    
    LA PRIMER LETRA DE LOS METODOS VA EN MINUSCULA POR BUENAS PRACTICAS
    
    el id es autogenerable creo asi que se va del parametro
     la fecha es autogenerable creo asi que se va del parametro
      el booleano es autogenerable creo asi que se va del parametro
    
        public void CrearComentario(String id, String contenido, Integer calificacion,
            LocalDateTime fechaHora, boolean altaBaja, Long idTrabajo) throws MiException {
    */
    
    @Transactional
    public void crearComentario(String contenido, Integer calificacion, Long idTrabajo) throws MiException {

        Optional<Trabajo> trabajoRespuesta = trabajoRepositorio.findById(idTrabajo);
      
        validarComentarios(contenido, calificacion);
       
        Date fecha = new Date();
        
        if (trabajoRespuesta.isPresent()) {
            
            Trabajo trabajo = trabajoRespuesta.get();
            
            //esto lo agregue para que ande
            //trabajo.setEstadoTrabajo(EstadoTrabajo.FINALIZADO);
            
            if (trabajo.getEstadoTrabajo() == EstadoTrabajo.FINALIZADO) {
               
                
                ///se agrego enum calificado y se setea calificado
                trabajo.setEstadoTrabajo(EstadoTrabajo.CALIFICADO);
                trabajoRepositorio.save(trabajo);
                
                
                
                    Comentario comentario = new Comentario(contenido, calificacion, fecha, true);
                               
                    comentario.setTrabajo(trabajo);                    
                    
                    comentarioRepositorio.save(comentario);

                    //bien ahi chicos !!! no se me ocurrio poner eso 
                
            } else {
                throw new MiException("El trabajo debe estar en estado FINALIZADO para agregar un comentario.");
            }
        } else {
            throw new MiException("No se encontró un trabajo con el ID proporcionado: " + idTrabajo);
        }
    }

//      @Transactional
//    public void CrearComentario(String contenido, Integer calificacion, Long idTrabajo) throws MiException {
//        Optional<Trabajo> trabajoRespuesta = trabajoRepositorio.findById(idTrabajo);
//     Date fecha =new Date();
//        
//        if (trabajoRespuesta.isPresent()) {
//            
//            Trabajo trabajo = trabajoRespuesta.get();
//          
//                if (trabajoRespuesta.isPresent()) {
//
//                    Comentario comentario = new Comentario(contenido, calificacion, fecha, true);
//                  
//                    comentario.setTrabajo(trabajo);
//                                        
//                    comentarioRepositorio.save(comentario);
//
//                } }
//    }
 @Transactional
    public void modificarComentarios(Long idTrabajo, String contenido, Integer calificacion, String id) throws MiException {

        
        Optional<Comentario> respuesta = comentarioRepositorio.findById(id);
        
        validarComentarios(contenido, calificacion);

        Date fecha = new Date();
        
        if (respuesta.isPresent()) {
            
            Comentario comentario = respuesta.get();
            if (comentario.isAltaBaja()) {
            comentario.setContenido(contenido);
            comentario.setCalificacion(calificacion);
            comentario.setFechaHora(fecha);            
            comentarioRepositorio.save(comentario);
            } else {
                throw new MiException ("El comentario está dado de baja, no se puede modificar.");
            }
        }
    }
    
    @Transactional
    public List<Comentario> ListaComentarios() {

        List<Comentario> comentarios = new ArrayList<>();

        comentarios = comentarioRepositorio.findAll();

        return comentarios;
    }
    //este lo agregue si no entienden pregunte
      @Transactional
    public List<Comentario> ListaComentariosPorProveedor(String idProveedor) {
        List<Comentario> comentarios = new ArrayList<>();
        comentarios = comentarioRepositorio.findAll();
          for (Comentario comentario : comentarios) {              
          } 
          
        List<Comentario> comentariosProveedor = new ArrayList<>();
        
          for (Comentario comentario : comentarios) {
              if(comentario.getTrabajo().getProveedor().getDni().equalsIgnoreCase(idProveedor)){
              comentariosProveedor.add(comentario);
              }
          }
        

        return comentariosProveedor;
    }
    
/*
    se los comente por que me me da errores, despues revisen en base a crear, tiene que quedar parecido
    @Transactional
    public void ModificarComentarios(String id, String contenido, Integer calificacion, boolean altaBaja, LocalDateTime fechaHora) throws MiException {

        ValidarComentarios(contenido, calificacion);
        Optional<Comentario> respuesta = comentarioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Comentario comentario = respuesta.get();

            comentario.setContenido(contenido);
            comentario.setCalificacion(calificacion);
            comentario.setFechaHora(fechaHora);
            comentarioRepositorio.save(comentario);
        }

    }*/

    @Transactional
    @Secured("ROLE_ADMIN")
    public void bajaComentario(String id) throws MiException {

        Optional<Comentario> comentarioRespuesta = comentarioRepositorio.findById(id);
       
        if (comentarioRespuesta.isPresent()) {
            Comentario comentario = comentarioRespuesta.get();

            // Verificar si ya está dado de baja
            if (comentario.isAltaBaja()) {
                comentario.setAltaBaja(false);
                comentarioRepositorio.save(comentario);
            } else {
                throw new MiException("El comentario ya está dado de baja.");
            }
        } else {
            throw new MiException("No se encontró un comentario con el ID proporcionado: " + id);
        }
    }
    //   }

    @Transactional
    private void validarComentarios(String contenido, Integer calificacion) throws MiException {

        if (contenido == null || contenido.isEmpty()) {
            throw new MiException(" El comentario no puede estar vacio, por favor complete este campo");

        }
        if (calificacion < 1 || calificacion > 5) {
            throw new MiException(" La calificacion no puede ser igual a cero, calificar entre 1 y 5");

        }
        //  List<Comentario> comentario = new ArrayList<>();
        // comentario = ListaComentarios();

    }
}

