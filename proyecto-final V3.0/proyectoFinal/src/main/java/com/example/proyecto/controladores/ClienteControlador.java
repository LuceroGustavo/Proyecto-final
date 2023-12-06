/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.proyecto.controladores;

<<<<<<< Updated upstream
=======
import com.example.proyecto.entidades.Cliente;
import com.example.proyecto.entidades.Persona;
>>>>>>> Stashed changes
import com.example.proyecto.entidades.Trabajo;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.servicios.ClienteServicio;
import com.example.proyecto.servicios.TrabajoServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author victo
 */
@Controller
@RequestMapping("/cliente")
public class ClienteControlador {
    
    @Autowired
    ClienteServicio clienteServicio;
    
    @Autowired
    TrabajoServicio trabajoServicio;
    
    @GetMapping("/registrar")
    public String registrar(){
    
return "cliente_form.html";
        }
    
      @PostMapping("/registro")
    public String registro(@RequestParam String nombre,String apellido,String dni,String telefono, 
 String direccion, String email,String password, ModelMap modelo,MultipartFile archivo, String password2){
        //se agrego string direccion
        try {
            clienteServicio.crearCliente(archivo, nombre, apellido, dni, telefono, email, password, password2, direccion);
            
            modelo.put("exito", "El Cliente fue registrado correctamente!");
        } catch (MiException ex) {
                      
            modelo.put("error", ex.getMessage());
            return "cliente_form.html";
        }
        
        return "index.html";        
    }
      ///definir si el proveedor puede contratar trabajos
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN','ROLE_PROVEEDOR')")
    @GetMapping("/listarTrabajo")
<<<<<<< Updated upstream
    public String listarTrabajo(ModelMap modelo) {
        
        String dniCliente = "t"; 
        
        List<Trabajo> listaTrabajos = trabajoServicio.listarTrabajoCliente(dniCliente);
        
        modelo.addAttribute("listaTrabajos",listaTrabajos);
        
        return "listar_trabajo_cliente.html";
    }
    
=======
    public String listarTrabajo(ModelMap modelo , HttpSession session) {
             
                 Persona cliente = (Persona) session.getAttribute("usuariosession");
                 
                  String dniCliente = cliente.getDni();
                         
        List<Trabajo> trabajos = trabajoServicio.listarTrabajoCliente(dniCliente);
                
        modelo.put("trabajos",trabajos);
        
        return "listar_trabajo_cliente.html";
    }
               
        
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo,HttpSession session){
        Cliente cliente = (Cliente) session.getAttribute("personaesession");
         modelo.put("cliente", cliente);
        return "cliente_modificar.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_ADMIN')")
    @PostMapping("/perfil/{id}")
    public String actualizar(@RequestParam String nombre,MultipartFile archivo, String apellido, String dni, String telefono,
    String email, String password, String password2, String domicilio, ModelMap modelo) {

        try {
            clienteServicio.actualizar(archivo, nombre, apellido, dni, telefono, email, password, password2, domicilio);

            modelo.put("exito", "Cliente actualizado correctamente!");

            return "inicio.html";
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
         

            return "cliente_modificar.html";
        }
    }
    
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
  
>>>>>>> Stashed changes
    
}
