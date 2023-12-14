package com.example.proyecto.servicios;

import com.example.proyecto.entidades.Comentario;
import com.example.proyecto.entidades.Proveedor;
import com.example.proyecto.entidades.Trabajo;
import com.example.proyecto.enumeraciones.EstadoTrabajo;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.repositorios.ComentarioRepositorio;
import com.example.proyecto.repositorios.ProveedorRepositorio;
import com.example.proyecto.repositorios.TrabajoRepositorio;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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

    @Autowired
    ProveedorRepositorio proveedorRepositorio;
    
    @Autowired
    WhatsappServicio whatsappServicio;

    private List<String> forbiddenWords = Arrays.asList("palabra1", "palabra2", "groseria1", "groseria2");

    public boolean containsForbiddenWords(String comment) {
        return forbiddenWords.stream().anyMatch(comment::contains);
    }

    @Transactional
    public void crearComentario(String contenido, Integer calificacion, Long idTrabajo) throws MiException {

        Optional<Trabajo> trabajoRespuesta = trabajoRepositorio.findById(idTrabajo);

        validarComentarios(contenido, calificacion);

        Date fecha = new Date();

        if (trabajoRespuesta.isPresent()) {

            Trabajo trabajo = trabajoRespuesta.get();
        
            if (trabajo.getEstadoTrabajo() == EstadoTrabajo.FINALIZADO) {

                Proveedor proveedor = trabajo.getProveedor();

                Integer contador = proveedor.getContdTrabajoRealizado();

                Integer califProm = proveedor.getPuntuacionPromedio();

                if(contador==null){
                contador=0;
                }
                                              
                Integer general = ((califProm * contador) + calificacion) / (contador + 1);

                proveedor.setContdTrabajoRealizado((contador + 1));

                proveedor.setPuntuacionPromedio(general);

                proveedorRepositorio.save(proveedor);

                trabajo.setEstadoTrabajo(EstadoTrabajo.CALIFICADO);
                
                
                         String mensaje = trabajo.getProveedor().getNombre()
                    + " " + trabajo.getProveedor().getApellido() + " Ha calificado tu Trabajo";
            whatsappServicio.Notoficacion(trabajo.getCliente().getTelefono(), mensaje);
                
                
                
                trabajoRepositorio.save(trabajo);

                Comentario comentario = new Comentario(contenido, calificacion, fecha, true);

                comentario.setTrabajo(trabajo);

                comentarioRepositorio.save(comentario);

                          } else {
                throw new MiException("El trabajo debe estar en estado FINALIZADO para agregar un comentario.");
            }
        } else {
            throw new MiException("No se encontró un trabajo con el ID proporcionado: " + idTrabajo);
        }
    }


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
                throw new MiException("El comentario está dado de baja, no se puede modificar.");
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
            if (comentario.getTrabajo().getProveedor().getDni().equalsIgnoreCase(idProveedor)) {
                comentariosProveedor.add(comentario);
            }
        }

        return comentariosProveedor;
    }

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

    //SE AGREGO AL ULTIMO
    //por fecha
    @Transactional
    public List<Comentario> ListaComentariosOrdenadosPorFecha() {
        List<Comentario> comentarios = ListaComentarios();

        Collections.sort(comentarios, Comparator.comparing(Comentario::getFechaHora));

        return comentarios;
    }

    // Calificación de menor a mayor
    @Transactional
    public List<Comentario> ListaComentariosOrdenadosPorCalificacion() {
        List<Comentario> comentarios = ListaComentarios();

        Collections.sort(comentarios, Comparator.comparingInt(Comentario::getCalificacion));

        return comentarios;
    }
    // Calificación de mayor a menor

    @Transactional
    public List<Comentario> ListaComentariosOrdenadosPorCalificacionMayor() {
        List<Comentario> comentarios = ListaComentarios();

        Collections.sort(comentarios, Comparator.comparingInt(Comentario::getCalificacion).reversed());

        return comentarios;
    }

    public void calificacionPromedio(String dniProveedor) throws MiException {
        // Obtener el proveedor por su DNI
        Proveedor proveedor = proveedorRepositorio.findById(dniProveedor)
                .orElseThrow(() -> new MiException("Proveedor no encontrado con DNI: " + dniProveedor));

        ///////////////////  ver este
        /*
        // Obtener todos los comentarios del proveedor
        List<Comentario> comentarios = comentarioRepositorio.findByTrabajoProveedorDniAndAltaBaja(dniProveedor, true);
         */
        List<Comentario> comentarios = new ArrayList();

        // Calcular la suma de las calificaciones
        int sumaCalificaciones = comentarios.stream()
                .mapToInt(Comentario::getCalificacion)
                .sum();

        // Calcular el promedio
        double promedioCalificaciones = comentarios.isEmpty() ? 0.0 : (double) sumaCalificaciones / comentarios.size();

        // Actualizar la puntuación promedio del proveedor
        proveedor.setPuntuacionPromedio((int) Math.round(promedioCalificaciones));

        // Guardar el proveedor actualizado
        proveedorRepositorio.save(proveedor);
    }   
}
