
package com.example.proyecto.servicios;

import com.example.proyecto.entidades.Imagen;
import com.example.proyecto.entidades.Proveedor;
import com.example.proyecto.entidades.Rubro;
import com.example.proyecto.enumeraciones.Rol;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.repositorios.ProveedorRepositorio;
import com.example.proyecto.repositorios.RubroRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProveedorServicio {

    @Autowired
    private ProveedorRepositorio proveedorRepositorio;
    
    @Autowired 
    private RubroRepositorio rubroRepositorio;
    
    @Autowired
    private ImagenServicio imagenServicio;
    
    //@Autowired
   // private RubroServicio rubroServicio; 
    
    @Transactional
    public void crearProveedor(MultipartFile archivo, String nombre, String apellido, String dni, String telefono, String email, String password,
                               String password2, String matricula, String descripcion,
                               double precioHora, String domicilio, String idRubro) throws MiException {
        //recordar el que rol se lo seteamos en el servicio. no lo traemos del controlador
                
        validar(nombre, apellido, dni, telefono, email, password, password2, precioHora, domicilio);
        
        Proveedor proveedor = new Proveedor();
        
        Rubro rubro = new Rubro();
        Optional<Rubro> rubroRespuesta = rubroRepositorio.findById(idRubro);
        
         if (rubroRespuesta.isPresent()) {
            rubro = rubroRespuesta.get();
         }
        proveedor.setRubro(rubro);

        proveedor.setAlta(true);
        proveedor.setMatricula(matricula);
        proveedor.setDomicilio(domicilio);
        proveedor.setNombre(nombre);
        proveedor.setApellido(apellido);
        proveedor.setDni(dni);
        proveedor.setTelefono(telefono);
        proveedor.setEmail(email);
        proveedor.setPassword(password);
        proveedor.setDescripcion(descripcion);        //Descripcion de cada proveedor Ej: horarios, presentación breve, etc.
        proveedor.setPrecioHora(precioHora); //Es el valor de los honorarios por hora.         
        proveedor.setFechaAlta(new Date());
        proveedor.setRol(Rol.PROVEEDOR);
        
        Imagen imagen = imagenServicio.guardar(archivo);

        proveedor.setImagen(imagen);
        
        proveedorRepositorio.save(proveedor);
    }
    
    @Transactional
    public void modificar(MultipartFile archivo, String nombre, String apellido, String dni, String telefono, String email, String password,
                               String password2, String matricula, String descripcion,
                               double precioHora, String idRubro, String domicilio) throws MiException {
        validar(nombre, apellido, dni, telefono, email, password, password2, precioHora, domicilio);
        // falta domicilio
        Optional<Proveedor> respuesta = proveedorRepositorio.findById(dni);
        if (respuesta.isPresent()) {

        Proveedor proveedor = respuesta.get();
        proveedor.setNombre(nombre);
        proveedor.setApellido(apellido);
        proveedor.setDni(dni);
        proveedor.setDomicilio(domicilio);
        proveedor.setTelefono(telefono);
        proveedor.setEmail(email);
        proveedor.setPassword(password);
        proveedor.setDescripcion(descripcion); 
        proveedor.setPrecioHora(precioHora);                
        proveedor.setRol(Rol.PROVEEDOR);         

            String idImagen = null;

            if (proveedor.getImagen() != null) {
                idImagen = proveedor.getImagen().getId();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

            proveedor.setImagen(imagen);

            proveedorRepositorio.save(proveedor);
           
        }
    }
   
    @Transactional(readOnly=true)
    public List<Proveedor> listarProveedores() {
       
     
        List<Proveedor> proveedores = new ArrayList<>();
 
        proveedores = proveedorRepositorio.findAll();
        List<Proveedor> proveedore = new ArrayList<>();
        for (Proveedor proveedor : proveedores) {
            if (proveedor.isAlta()) {
                proveedore.add(proveedor);
            }
        }
        return proveedore;

    }

    @Transactional(readOnly = true)
    public List<Proveedor> listarPorRubro(String rubro) {

        List<Proveedor> proveedores = listarProveedores();

        List<Proveedor> proveedoresPorRubro = new ArrayList<>();

        for (Proveedor proveedor : proveedores) {
            if (proveedor.getRubro().getNombreRubro().equalsIgnoreCase(rubro)) {
                proveedoresPorRubro.add(proveedor);
            }
        }
        return proveedoresPorRubro;

    }
    
    @Transactional
    public void cambiarRolCliente(String dni) throws MiException {
    Optional<Proveedor> proveedorOptional = proveedorRepositorio.findById(dni);

    if (proveedorOptional.isPresent()) {
        Proveedor proveedor = proveedorOptional.get();

        // Verificar si el proveedor está dado de alta antes de realizar el cambio de rol
        if (proveedor.isAlta()) {
            proveedor.setRol(Rol.CLIENTE);
            proveedorRepositorio.save(proveedor);
        } else {
            throw new MiException("El proveedor con DNI " + dni + " no está dado de alta.");
        }
    } else {
        throw new MiException("No se encontró un proveedor con el DNI proporcionado: " + dni);
    }
}
    
    
    @Transactional
    public void eliminar(String dni) throws MiException {

        Optional<Proveedor> proveedorOptional = proveedorRepositorio.findById(dni);
        if (proveedorOptional.isPresent()) {
            Proveedor proveedor = proveedorOptional.get();
//
//            // Eliminar la imagen asociada al proveedor
//            if (proveedor.getImagen() != null) {
//                imagenServicio.eliminar(proveedor.getImagen().getId());
//            }

            // Eliminar el proveedor de la base de datos
            proveedor.setAlta(false);
            //proveedorRepositorio.delete(proveedor);
        } else {
            throw new MiException("No se encontró un proveedor con el DNI proporcionado: " + dni);
        }
    }

    public List<Proveedor> listAll(String palabraClave) {
        if (palabraClave != null) {
            return proveedorRepositorio.findAll(palabraClave);
        }
        return proveedorRepositorio.findAll();
    }


    @Transactional   
    private void validar(String nombre, String apellido, String dni, String telefono,
            String email, String password, String password2, double precioHora, String domicilio) throws MiException {
        
        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser nulo o estar vacío");
        }

        if (apellido == null || apellido.isEmpty()) {
            throw new MiException("El apellido no puede ser nulo o estar vacío");
        }

        if (dni == null || dni.isEmpty()) {
            throw new MiException("El DNI no puede ser nulo o estar vacío");            
        }
        
        if (telefono == null || telefono.isEmpty()) {
            throw new MiException("El teléfono no puede ser nulo o estar vacío");
        }

        if (email == null || email.isEmpty()) {
            throw new MiException("El email no puede ser nulo o estar vacío");
        }

        if (password == null || password.isEmpty() || password.length() <= 5) {
            throw new MiException("La contraseña no puede estar vacía y debe tener más de 5 dígitos");
        }  

        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }

        if (precioHora == 0.0) {
            throw new MiException("El campo honorarios/hora no puede estar vacío o ser cero");
        }
        
        if (proveedorRepositorio.existsByDni(dni)) {
            throw new MiException("El DNI ya está registrado en el sistema");
        }

        if (proveedorRepositorio.existsByEmail(email)) {
            throw new MiException("El email ya está registrado en el sistema");
        }
        if (domicilio == null || domicilio.isEmpty()) {
            throw new MiException("El domicilio no estar vacío");
    }
 
 }

}