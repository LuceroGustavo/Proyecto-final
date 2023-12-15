package com.example.proyecto.servicios;

import com.example.proyecto.entidades.Cliente;
import com.example.proyecto.entidades.Imagen;
import com.example.proyecto.entidades.Persona;
import com.example.proyecto.entidades.Proveedor;
import com.example.proyecto.enumeraciones.Rol;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.repositorios.ClienteRepositorio;
import com.example.proyecto.repositorios.PersonaRepositorio;
import com.example.proyecto.repositorios.ProveedorRepositorio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

//para ver entre todos
@Service
public class ClienteServicio {

    @Autowired
    ClienteRepositorio clienteRepositorio;
    @Autowired
    ImagenServicio imagenServicio;
    @Autowired
    ProveedorServicio proveedorServicio;
    @Autowired
    ProveedorRepositorio proveedorRepositorio;
         @Autowired
    private PersonaRepositorio personaRepositorio;

    @Transactional
    public void crearCliente(MultipartFile archivo, String nombre, String apellido, String dni, String telefono,
            String email, String password, String password2, String domicilio) throws MiException {

        validar(nombre, apellido, dni, telefono, email, password, password2, domicilio);
        Cliente cliente = new Cliente();
        cliente.setDomicilio(domicilio);
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setDni(dni);
        cliente.setTelefono(telefono);
        cliente.setEmail(email);
        cliente.setPassword(new BCryptPasswordEncoder().encode(password));
        cliente.setAlta(true);
        cliente.setFechaAlta(new Date());
        
                    if (personaRepositorio.count()==0){
cliente.setRol(Rol.ADMIN);}
else {
        cliente.setRol(Rol.CLIENTE);
             }
       

        // llamamos al metodo guardar q se encargara de guardar la foto en la base de
        // dato.
        Imagen imagen = imagenServicio.guardar(archivo);
        cliente.setImagen(imagen);

        clienteRepositorio.save(cliente);

    }

    @Transactional

    public void cambiarRol(String id, boolean autorizaicion) { //GUSTAVO Y VICTOR

        Optional<Cliente> respuesta = clienteRepositorio.findById(id);
        if (respuesta.isPresent() && autorizaicion) {
            Cliente cliente = respuesta.get();
            Proveedor proveedor = new Proveedor();

            // SE CREA UN N UEVO PROVEEDOR  
            proveedor.setAlta(true);
            proveedor.setNombre(cliente.getNombre());
            proveedor.setApellido(cliente.getApellido());
            proveedor.setDni(cliente.getDni());
            proveedor.setDomicilio(cliente.getDomicilio());
            proveedor.setTelefono(cliente.getTelefono());
            proveedor.setEmail(cliente.getEmail());
            proveedor.setFechaAlta(cliente.getFechaAlta());
            proveedor.setPassword(cliente.getPassword());
            // proveedor.setPassword(new BCryptPasswordEncoder().encode(cliente.getPassword()));
            proveedor.setImagen(cliente.getImagen());
            proveedor.setRol(Rol.PROVEEDOR);

            // crear mensaje de cambio relizado con exito e informar alusuario que debe completar los datos de proveedor faltantes
            proveedorRepositorio.save(proveedor);

        }

    }

    @Transactional
    public void validar(String nombre, String apellido, String dni, String telefono, String email, String password,
            String password2, String domicilio) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el Nombre no puede ser nulo o estar vacío");
        }

        //INVErtir todos para evitar el error en amarillo
        if (apellido == null || apellido.isEmpty()) {
            throw new MiException("El Apellido no puede ser nulo o estar vacio");
        }

        if (domicilio.isEmpty() || domicilio == null) {
            throw new MiException("El domicilio no puede ser nulo o estar vacio");
        }

        if (dni.isEmpty() || dni == null) {
            throw new MiException("El DNI no puede ser nulo o estar vacio");
        }

        if (clienteRepositorio.existsByDni(dni)) {
            throw new MiException("El DNI ya está registrado en el sistema");
        }

        if (telefono.isEmpty() || telefono == null) {
            throw new MiException("El Telefono no puede ser nulo o estar vacio");
        }
        if (email.isEmpty() || email == null) {
            throw new MiException("el Email no puede ser nulo o estar vacio");
        }
        if (clienteRepositorio.existsByEmail(email)) {
            throw new MiException("El Email ya se encuentra registrado en el sistema");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");

            // para mas adelante hacer un validador de contarseñas ejemplo que no se a000000
            // 123456 etc
        }

        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }

    }

    public Cliente getOne(String id) {
        return clienteRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarCliente() {

        List<Cliente> clientes = new ArrayList<>();

        clientes = clienteRepositorio.findAll();

        return clientes;
    }

    // Recupera la informacion del cliente por el email
    // Y lo devuelve en el tipo de dato Cliente
    @Transactional
    public Cliente getClienteByEmail(String email) {
        Cliente clienteFound = clienteRepositorio.BuscarPorEmail(email);
        return clienteFound;

    }

    // Modificar Cliente
    @Transactional
    public void actualizar(MultipartFile archivo, String nombre, String apellido, String dni, String telefono,
            String email, String domicilio) throws MiException {
        validar2(nombre, apellido, dni, email, telefono, domicilio);
        Optional<Cliente> respuesta = clienteRepositorio.findById(dni);// findById(dni)**: Este método busca un objeto
        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setTelefono(telefono);
            cliente.setEmail(email);
            cliente.setDomicilio(domicilio);
            String idImagen = null;
            if (cliente.getImagen() != null && !archivo.isEmpty()) {

                idImagen = cliente.getImagen().getId();

                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

                cliente.setImagen(imagen);
            }

            clienteRepositorio.save(cliente);

        }
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

            if (proveedor.getImagen() != null && !archivo.isEmpty()) {

                idImagen = proveedor.getImagen().getId();

                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

                proveedor.setImagen(imagen);
            }

            proveedorRepositorio.save(proveedor);

        }
    }

    @Transactional
    public void validar2(String nombre, String apellido, String dni, String telefono, String email,
            String domicilio) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }
        if (apellido.isEmpty() || apellido == null) {
            throw new MiException("El Apellido no puede ser nulo o estar vacio");
            //    throw new MiException("El nombre no puede ser nulo o estar vacio");
        }

        if (domicilio.isEmpty() || domicilio == null) {
            throw new MiException("El domicilio no puede ser nulo o estar vacio");
        }

        if (telefono.isEmpty() || telefono == null) {
            throw new MiException("El telefono no puede ser nulo o estar vacio");
        }
        if (email.isEmpty() || email == null) {
            throw new MiException("el email no puede ser nulo o estar vacio");
        }
    }

    @Transactional
    public void delete(String dni) {
        try {
            clienteRepositorio.deleteById(dni);
        } catch (Exception e) {
        }
    }

    public void cambiarEstadoCliente(String dni, boolean activar) throws MiException { // VICTOR

        Optional<Cliente> clienteRepuest = clienteRepositorio.findById(dni);
        Cliente cliente = new Cliente();

        if (clienteRepuest.isPresent()) {
            cliente = clienteRepuest.get();
            cliente.setAlta(activar);

        } else {
            throw new MiException("No se encontró el cliente con el DNI proporcionado: " + dni);

        }

    }

}
