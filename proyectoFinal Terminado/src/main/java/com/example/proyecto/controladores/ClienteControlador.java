package com.example.proyecto.controladores;

import com.example.proyecto.entidades.Cliente;
import com.example.proyecto.entidades.Persona;
import com.example.proyecto.entidades.Proveedor;
import com.example.proyecto.entidades.Rubro;
import com.example.proyecto.entidades.Trabajo;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.servicios.ClienteServicio;
import com.example.proyecto.servicios.ProveedorServicio;
import com.example.proyecto.servicios.RubroServicio;
import com.example.proyecto.servicios.TrabajoServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/cliente")
public class ClienteControlador {

    @Autowired
    ClienteServicio clienteServicio;

    @Autowired
    ProveedorServicio proveedorServicio;

    @Autowired
    TrabajoServicio trabajoServicio;
    @Autowired
    RubroServicio rubroServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) {

        return "cliente_form.html";
    }

    @PostMapping("/registro")
    public String registro(String validador, String nombre, String apellido, String dni, String telefono,
            String direccion, String email, String password, String matricula,
            Double precioHora, String descripcion,
            ModelMap modelo, MultipartFile archivo, String password2, String idRubro) {

        try {
            if (validador.equalsIgnoreCase("1")) {
                proveedorServicio.crearProveedor(archivo, nombre, apellido, dni, telefono, email,
                        password, password2, matricula, descripcion, precioHora, direccion, idRubro);
                modelo.put("exito", "El Proveedor fue registrado correctamente!");

            } else {

                clienteServicio.crearCliente(archivo, nombre, apellido, dni, telefono, email, password, password2, direccion);
                modelo.put("exito", "El Cliente fue registrado correctamente!");

            }

        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("dni", dni);
            modelo.put("telefono", telefono);
            modelo.put("direccion", direccion);
            modelo.put("email", email);
            modelo.put("precioHora", precioHora);
            modelo.put("matricula", matricula);
            modelo.put("idRubro", idRubro);
            modelo.put("validador", validador);
            modelo.put("descripcion", descripcion);
            return "registroDoble.html";
        }

        return "login1.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN','ROLE_PROVEEDOR')")
    @GetMapping("/listarTrabajo")
    public String listarTrabajo(ModelMap modelo, HttpSession session) {

        Persona cliente = (Persona) session.getAttribute("usuariosession");
        List<Trabajo> trabajos = trabajoServicio.listarTrabajoCliente(cliente.getDni());
        modelo.put("trabajos", trabajos);

        return "listar_trabajo_cliente.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("personaesession");
        modelo.put("cliente", cliente);
        return "cliente_modificar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_ADMIN')")
    @PostMapping("/perfil/{id}")
    public String actualizar(@RequestParam String nombre, MultipartFile archivo, String apellido, String dni, String telefono,
            String email, String domicilio, ModelMap modelo) {

        try {
            clienteServicio.actualizar(archivo, nombre, apellido, dni, telefono, email, domicilio);

            modelo.put("exito", "Cliente actualizado correctamente!");

            return "inicio.html";
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());

            return "cliente_modificar.html";
        }
    }

    /* 
    @GetMapping("/modificar")
    public String goToModificarCliente(ModelMap modelo, Principal principal){
        Cliente cliente = clienteServicio.getClienteByEmail(principal.getName());
         modelo.addAttribute("user",cliente);
        return "modificarCliente.html";
        }



      @PostMapping("/modificar1")
    public String ModificarUsuario(@RequestParam String nombre,String apellido,String dni,String telefono, 
 String domicilio,String password, ModelMap modelo,MultipartFile archivo, String password2, Principal principal){

            // Se recupera el email del usuario logueado
            String emailUsuario = principal.getName();
            
            // Se recupupera el cliente de la base de datos por email
            Cliente clienteEncontrado = clienteServicio.getClienteByEmail(emailUsuario);
            
            // Se modifican las propiedades que queremos modificar del cliente
            clienteEncontrado.setNombre(nombre);
            clienteEncontrado.setApellido(apellido);
            clienteEncontrado.setDni(dni);
            clienteEncontrado.setDomicilio(domicilio);

            // Se llama al ActualizarCliente, pasandole el cliente modificado
          //  clienteServicio.actulizarCliente(clienteEncontrado);
       
            // Se retorna al index
        return "index1.html";        
    }
     */
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {
            modelo.put("error", "Cliente o Contraseña invalidos!");
        }

        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN','ROLE_PROVEEDOR')")
    @GetMapping("/modificar")
    public String modificar(ModelMap modelo, HttpSession session) {

        Persona persona = (Persona) session.getAttribute("usuariosession");

        if (persona instanceof Cliente) {
            
Cliente cliente=clienteServicio.getOne(persona.getDni());
        
            modelo.put("cliente", cliente);

            modelo.put("verificador", 3);
            return "modificarDatos.html";

        } else {
            List<Rubro> rubros = rubroServicio.ListaRubros();
            modelo.addAttribute("rubros", rubros);
            modelo.put("verificador", 2);

            Proveedor proveedor = proveedorServicio.getOne(persona.getDni());
            modelo.put("cliente", proveedor);

            return "modificarDatos.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN','ROLE_PROVEEDOR')")
    @PostMapping("/modificado")
    public String actualizar(String dni, String nombre, MultipartFile archivo, String apellido, String telefono,
            String email, String domicilio, ModelMap modelo, HttpSession session) {
       
        try {
            clienteServicio.actualizar(archivo, nombre, apellido, dni, telefono, email, domicilio);
            
     Cliente clienteActualizado = clienteServicio.getOne(dni);
      // Actualizar la información del usuario en la sesión
     session.setAttribute("usuariosession", clienteActualizado);
     
            modelo.put("exito", "Cliente actualizado correctamente!");

            return "index1.html";
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());

            return "modificarCliente.html";
        }

    }

     @PreAuthorize("hasRole('ROLE_ADMIN')")
       @GetMapping("/listarTodos")
    public String listartodos( ModelMap modelo) {

      List <Cliente> clientes = clienteServicio.listarCliente();
modelo.put("clientes", clientes);
        return "Cliente_list2.html";
    }
    
    
}
