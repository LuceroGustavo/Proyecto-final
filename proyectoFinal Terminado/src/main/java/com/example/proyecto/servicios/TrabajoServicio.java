package com.example.proyecto.servicios;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.proyecto.entidades.Cliente;
import com.example.proyecto.entidades.Persona;
import com.example.proyecto.entidades.Proveedor;
import com.example.proyecto.entidades.Trabajo;
import com.example.proyecto.enumeraciones.EstadoTrabajo;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.repositorios.ClienteRepositorio;
import com.example.proyecto.repositorios.PersonaRepositorio;
import com.example.proyecto.repositorios.ProveedorRepositorio;
import com.example.proyecto.repositorios.TrabajoRepositorio;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrabajoServicio {

    Trabajo trabajo = new Trabajo();
    Cliente cliente = new Cliente();
    Proveedor proveedor = new Proveedor();
    Persona persona = new Persona();

    @Autowired
    ClienteRepositorio clienteRepositorio;
    @Autowired
    ProveedorRepositorio proveedorRepositorio;
    @Autowired
    TrabajoRepositorio trabajoRepositorio;
    @Autowired
    PersonaRepositorio personaRepositorio;
    @Autowired
    WhatsappServicio whatsappServicio;

    @Transactional
    public void solicitudTrabajo(String dniProveedor, String dniCliente, String detalleDeTrabajo) throws MiException {

        validarDetalleTrabajo(detalleDeTrabajo);

        Persona persona1 = new Persona();
        Proveedor proveedor1 = new Proveedor();
        Trabajo trabajo1 = new Trabajo();

        Optional<Persona> clienteRespuesta = personaRepositorio.findById(dniCliente);

        if (clienteRespuesta.isPresent()) {
            persona1 = clienteRespuesta.get();

        }
        Optional<Proveedor> proveedorRespuesta = proveedorRepositorio.findById(dniProveedor);

        if (proveedorRespuesta.isPresent()) {
            proveedor1 = proveedorRespuesta.get();
        }

        trabajo1.setProveedor(proveedor1);
        // trabajo.setCliente(cliente);
        trabajo1.setCliente(persona1);
        trabajo1.setEstadoTrabajo(EstadoTrabajo.PENDIENTE);
        trabajo1.setDetalleDeSolicitud(detalleDeTrabajo);
        trabajo1.setPrecioFinal(0d);
        trabajoRepositorio.save(trabajo1);

        String mensaje = "Has recibido una nueva solicitud de trabajo de " + trabajo1.getCliente().getNombre()
                + " " + trabajo1.getCliente().getApellido();

        whatsappServicio.Notoficacion(trabajo1.getProveedor().getTelefono(), mensaje);

    }

    @Transactional
    public void revisionDeTrabajo(Long idtrabajo, Boolean aceptacion,
            String respuestaProveedor, Integer gastosAdicionales, Integer horasTrabajadasEstimadas, String dniProveedor) throws MiException {

        validarRevision(respuestaProveedor, gastosAdicionales, horasTrabajadasEstimadas);

        Trabajo trabajo = new Trabajo();

        Optional<Trabajo> trabajoRespuesta = trabajoRepositorio.findById(idtrabajo);

        if (trabajoRespuesta.isPresent()) {
            trabajo = trabajoRespuesta.get();
        }

        if (aceptacion) {
            trabajo.setEstadoTrabajo(EstadoTrabajo.REVISION);
            trabajo.setFechaInicio(new Date());

            trabajo.setRespuestaProveedor(respuestaProveedor);
            trabajo.setHorasTrabajoEstimadas(horasTrabajadasEstimadas);
            trabajo.setGastosAdicionales(gastosAdicionales);

            Optional<Proveedor> proveedorRespuesta = proveedorRepositorio.findById(dniProveedor);

            if (proveedorRespuesta.isPresent()) {
                proveedor = proveedorRespuesta.get();
            }

            Double preciofinal;

            preciofinal = (proveedor.getPrecioHora() * trabajo.getHorasTrabajoEstimadas()) + trabajo.getGastosAdicionales();

            trabajo.setPrecioFinal(preciofinal);

        } else {
            trabajo.setEstadoTrabajo(EstadoTrabajo.CANCELADO);
            trabajo.setRespuestaProveedor(respuestaProveedor);
        }

        String mensaje = trabajo.getProveedor().getNombre()
                + " " + trabajo.getProveedor().getApellido() + " Ha presupuestado tu Trabajo";
        whatsappServicio.Notoficacion(trabajo.getCliente().getTelefono(), mensaje);

        trabajoRepositorio.save(trabajo);
    }

    @Transactional
    public void aceptacionCliente(Long idTrabajo, boolean aceptacion) throws MiException {
        Optional<Trabajo> trabajoRespuesta = trabajoRepositorio.findById(idTrabajo);

        if (trabajoRespuesta.isPresent()) {
            Trabajo trabajo = trabajoRespuesta.get();

            if (aceptacion) {
                trabajo.setEstadoTrabajo(EstadoTrabajo.ACEPTADO);

                // Obtener el proveedor asociado al trabajo
                Proveedor proveedor = trabajo.getProveedor();

                // Cambiar el estado del proveedor a ocupado
                proveedor.setEstadoActual(false);

            } else {
                trabajo.setEstadoTrabajo(EstadoTrabajo.CANCELADO);
            }

            String mensaje = trabajo.getProveedor().getNombre()
                    + " " + trabajo.getProveedor().getApellido() + " Ha Aceptado tu Presupesto";
            whatsappServicio.Notoficacion(trabajo.getCliente().getTelefono(), mensaje);

            trabajoRepositorio.save(trabajo);

            trabajoRepositorio.save(trabajo);
        } else {
            throw new MiException("No se encontró un trabajo con el ID proporcionado: " + idTrabajo);
        }
    }

    //eliminar idProvedor
    // proveedor es con dos EE
    // recordar setear el contador de trabajos fginalizados!!!!!!!!!!!!
    @Transactional
    public void finalizadoTrabajo(Long idTrabajo, String idProveedor, Boolean estado) throws MiException {
        Trabajo trabajo = new Trabajo();
        Proveedor proveedor = new Proveedor();
        Optional<Trabajo> trabajoRespuesta = trabajoRepositorio.findById(idTrabajo);
        Optional<Proveedor> proveedorRespuesta = proveedorRepositorio.findById(idProveedor);

        if (trabajoRespuesta.isPresent()) {
            trabajo = trabajoRespuesta.get();

            trabajo.setEstadoTrabajo(EstadoTrabajo.FINALIZADO);
            trabajoRepositorio.save(trabajo);

            String mensaje = trabajo.getProveedor().getNombre()
                    + " " + trabajo.getProveedor().getApellido() + " Ha dado por Finalizado el Trabajo";
            whatsappServicio.Notoficacion(trabajo.getCliente().getTelefono(), mensaje);
        }

        if (proveedorRespuesta.isPresent()) {
            proveedor = proveedorRespuesta.get();

        }

        if (estado) {
            proveedor.setEstadoActual(true);
            proveedorRepositorio.save(proveedor);
        }
    }

    public void CancelarTrabajo(Long idTrabajo, String idProveedor, Boolean estado) throws MiException {
        Trabajo trabajo = new Trabajo();
        Proveedor proveedor = new Proveedor();
        Optional<Trabajo> trabajoRespuesta = trabajoRepositorio.findById(idTrabajo);
        Optional<Proveedor> proveedorRespuesta = proveedorRepositorio.findById(idProveedor);

        if (trabajoRespuesta.isPresent()) {
            trabajo = trabajoRespuesta.get();

            String mensaje = trabajo.getProveedor().getNombre()
                    + " " + trabajo.getProveedor().getApellido() + " Ha Cancelado el Trabajo";
            whatsappServicio.Notoficacion(trabajo.getCliente().getTelefono(), mensaje);

            trabajo.setEstadoTrabajo(EstadoTrabajo.CANCELADO);
            trabajoRepositorio.save(trabajo);
        }

        if (proveedorRespuesta.isPresent()) {
            proveedor = proveedorRespuesta.get();

        }

        if (estado) {
            proveedor.setEstadoActual(true);
            proveedorRepositorio.save(proveedor);
        }
    }

///no se si se usa creo esta en comentario sevicio ver de migrarlo ahi
    @Transactional
    public void comentarioYpuntuacion(Long idTrabajo, String comentario, int puntuacion) throws MiException {
        Optional<Trabajo> trabajoRespuesta = trabajoRepositorio.findById(idTrabajo);

        if (trabajoRespuesta.isPresent()) {
            Trabajo trabajo = trabajoRespuesta.get();

            if (trabajo.getEstadoTrabajo() == EstadoTrabajo.FINALIZADO) {
                if (puntuacion > 0 && puntuacion <= 5) {
                    trabajo.setPuntuacionTrabajo(puntuacion);
                    trabajo.setCometarioTrabajoTerminado(comentario);
                    trabajoRepositorio.save(trabajo);
                    //es mas facil validar los datos en otro metodo
                } else {
                    throw new MiException("La puntuación debe estar entre 1 y 5");
                }
            } else {
                throw new MiException("Debe estar finalizado el trabajo para poder dar el comentario y puntuacion");
            }
        } else {
            throw new MiException("No se encontró un trabajo con el ID proporcionado: " + idTrabajo);
        }
    }

    public List<Trabajo> listarTrabajoCliente(String dniCliente) {

        System.out.println(dniCliente);
        List<Trabajo> trabajosCliente = new ArrayList<>();
        List<Trabajo> trabajos1 = trabajoRepositorio.findAll();
        for (Trabajo trabajo1 : trabajos1) {
            if (trabajo1.getCliente().getDni().equalsIgnoreCase(dniCliente)) {
                trabajosCliente.add(trabajo1);
            }
        }

        return trabajosCliente;
    }

    public List<Trabajo> listarTrabajoProveedor(String dniProveedor) {

        System.out.println(dniProveedor);
        List<Trabajo> trabajosProveedor = new ArrayList<>();
        List<Trabajo> trabajos1 = trabajoRepositorio.findAll();

        //ver por que no anda este metodo
        //  trabajosProveedor = trabajoRepositorio.findByProveedorDni(dniProveedor);
        for (Trabajo trabajo1 : trabajos1) {
            if (trabajo1.getProveedor().getDni().equalsIgnoreCase(dniProveedor)) {
                trabajosProveedor.add(trabajo1);
            }
        }
        return trabajosProveedor;
    }

    public Trabajo getOne(Long idTrabajo) {
        Trabajo trabajo = trabajoRepositorio.getOne(idTrabajo);
        return trabajo;
    }

    public void validarDetalleTrabajo(String detalleDeTrabajo) throws MiException {

        if (detalleDeTrabajo == null || detalleDeTrabajo.isEmpty()) {
            throw new MiException("Por favor, tiene que detallar el trabajo que necesita.");
        }

    }

    public void validarRevision(String respuestaProveedor, Integer gastosAdicionales,
            Integer horasTrabajadasEstimadas) throws MiException {

        if (respuestaProveedor == null || respuestaProveedor.isEmpty()) {
            throw new MiException("Por favor, describa el trabajo que realizará.");
        }
        if (gastosAdicionales < 0) {
            throw new MiException("Los gastos adicionales no pueden ser menores que cero.");
        }

        if (horasTrabajadasEstimadas <= 0) {
            throw new MiException("La estimación de las horas trabajadas no puede ser igual a cero.");
        }

    }

    public List<Trabajo> listarTodos() {
        List<Trabajo> trabajos = trabajoRepositorio.findAll();
        return trabajos;
    }

    public List<Trabajo> listarFinalizados() {
        List<Trabajo> trabajos = trabajoRepositorio.findAll();

        for (Trabajo trabajo1 : trabajos) {

            if (trabajo.getEstadoTrabajo() == EstadoTrabajo.FINALIZADO) {
                trabajos.add(trabajo1);
            }

        }
        return trabajos;
    }

    public List<Trabajo> listarRechazados() {
        List<Trabajo> trabajos = trabajoRepositorio.findAll();

        for (Trabajo trabajo1 : trabajos) {

            if (trabajo.getEstadoTrabajo() == EstadoTrabajo.CANCELADO) {
                trabajos.add(trabajo1);
            }

        }
        return trabajos;
    }

    public List<Trabajo> listarAceptados() {
        List<Trabajo> trabajos = trabajoRepositorio.findAll();

        for (Trabajo trabajo1 : trabajos) {

            if (trabajo.getEstadoTrabajo() == EstadoTrabajo.ACEPTADO) {
                trabajos.add(trabajo1);
            }

        }
        return trabajos;
    }
}
