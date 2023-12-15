package com.example.proyecto.servicios;

import com.example.proyecto.entidades.Cliente;
import com.example.proyecto.entidades.Imagen;
import com.example.proyecto.entidades.Proveedor;
import com.example.proyecto.entidades.Rubro;
import com.example.proyecto.enumeraciones.Rol;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.repositorios.PersonaRepositorio;
import com.example.proyecto.repositorios.ProveedorRepositorio;
import com.example.proyecto.repositorios.RubroRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
       @Autowired
    private PersonaRepositorio personaRepositorio;

    @Transactional
    public void crearProveedor(MultipartFile archivo, String nombre,
            String apellido, String dni, String telefono, String email, String password,
            String password2, String matricula, String descripcion,
            Double precioHora, String domicilio, String idRubro) throws MiException {
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
        proveedor.setPassword(new BCryptPasswordEncoder().encode(password));
        proveedor.setDescripcion(descripcion);        //Descripcion de cada proveedor Ej: horarios, presentación breve, etc.
        proveedor.setPrecioHora(precioHora); //Es el valor de los honorarios por hora.         
        proveedor.setFechaAlta(new Date());
             if (personaRepositorio.count()==0){
proveedor.setRol(Rol.ADMIN);}
else {
        proveedor.setRol(Rol.PROVEEDOR);
             }
        Imagen imagen = imagenServicio.guardar(archivo);

        proveedor.setImagen(imagen);

        proveedor.setImagen(imagen);
        proveedorRepositorio.save(proveedor);
    }

    @Transactional
    public void modificar(MultipartFile archivo, String nombre,
            String apellido, String dni, String telefono, String email,
            String domicilio) throws MiException {
        validar2(nombre, apellido, dni, telefono, email, domicilio);
     
        Optional<Proveedor> respuesta = proveedorRepositorio.findById(dni);
        if (respuesta.isPresent()) {

            Proveedor proveedor = respuesta.get();
            proveedor.setNombre(nombre);
            proveedor.setApellido(apellido);
            proveedor.setDni(dni);
            proveedor.setDomicilio(domicilio);
            proveedor.setTelefono(telefono);
            proveedor.setEmail(email);

            String idImagen = null;

            if (proveedor.getImagen() != null  && !archivo.isEmpty()) {

                idImagen = proveedor.getImagen().getId();
                
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

            proveedor.setImagen(imagen);
            }
            

            proveedorRepositorio.save(proveedor);

        }
    }

    @Transactional
    public void modificarOficio(String dni,
            String matricula, String descripcion,
            Double precioHora, String idRubro) throws MiException {
        validar3(precioHora, idRubro, matricula,descripcion);

        Proveedor proveedor = null;
        // falta domicilio
        Optional<Proveedor> respuesta = proveedorRepositorio.findById(dni);

        if (respuesta.isPresent()) {
            proveedor = respuesta.get();
            proveedor.setDescripcion(matricula);
            proveedor.setDescripcion(descripcion);
            proveedor.setPrecioHora(precioHora);
            proveedorRepositorio.save(proveedor);
            Optional<Rubro> respuestaRubro = rubroRepositorio.findById(idRubro);

            if (respuestaRubro.isPresent()) {
                Rubro rubro = respuestaRubro.get();
                proveedor.setRubro(rubro);
            }
            proveedorRepositorio.save(proveedor);
        }

    }

    @Transactional
    private void validar2(String nombre, String apellido, String dni, String telefono,
            String email, String domicilio) throws MiException {

        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser nulo o estar vacío");
        }

        if (apellido == null || apellido.isEmpty()) {
            throw new MiException("El apellido no puede ser nulo o estar vacío");
        }

        if (telefono == null || telefono.isEmpty()) {
            throw new MiException("El teléfono no puede ser nulo o estar vacío");
        }

        if (email == null || email.isEmpty()) {
            throw new MiException("El email no puede ser nulo o estar vacío");
        }

           if (domicilio == null || domicilio.isEmpty()) {
            throw new MiException("El email no puede ser nulo o estar vacío");
        }
          if (proveedorRepositorio.existsByEmail(email)) {
            throw new MiException("El email ya está registrado en el sistema");
        }
           
        
    }

    @Transactional(readOnly = true)
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

    public void cambiarEstadoPoveedor(String dni, boolean activar) throws MiException { // VICTOR

        Optional<Proveedor> proveedorRepuest = proveedorRepositorio.findById(dni);
        Proveedor proveedor = new Proveedor();

        if (proveedorRepuest.isPresent()) {
            proveedor = proveedorRepuest.get();
            proveedor.setAlta(activar);

        } else {
            throw new MiException("No se encontró el cliente con el DNI proporcionado: " + dni);

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
            String email, String password, String password2, Double precioHora, String domicilio) throws MiException {

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
            throw new MiException("El domicilio no puede estar vacío");
        }

    }

    @Transactional
    public void validar3(Double precioHora, String idRubro, String matricula, String descripcion) throws MiException {

        if (precioHora == null || precioHora == 0.0) {
            throw new MiException("El campo honorarios/hora no puede estar vacío o ser cero");
        }

          if (descripcion == null || descripcion.isEmpty()) {
            throw new MiException("El campo honorarios/hora no puede estar vacío o ser cero");
        }
        
        if (idRubro == null || idRubro.isEmpty()) {
            throw new MiException("debes seleccionar un rubro");
        }

        if (matricula == null || matricula.isEmpty()) {
            throw new MiException("debes ingresar una matricula");
        }
    }

    public Proveedor getOne(String dni) {
        return proveedorRepositorio.getOne(dni);
    }

}
