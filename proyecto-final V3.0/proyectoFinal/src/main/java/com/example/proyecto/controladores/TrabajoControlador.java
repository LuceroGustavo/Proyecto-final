package com.example.proyecto.controladores;

import com.example.proyecto.entidades.Cliente;
import com.example.proyecto.entidades.Persona;
import com.example.proyecto.entidades.Proveedor;
import com.example.proyecto.entidades.Trabajo;
import com.example.proyecto.excepciones.MiException;
<<<<<<< Updated upstream
import com.example.proyecto.servicios.ProveedorServicio;
import com.example.proyecto.servicios.TrabajoServicio;
=======
import com.example.proyecto.servicios.ClienteServicio;
import com.example.proyecto.servicios.ComentarioServicio;
import com.example.proyecto.servicios.ProveedorServicio;
import com.example.proyecto.servicios.TrabajoServicio;
import java.time.LocalDateTime;
import java.util.List;
>>>>>>> Stashed changes
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN','ROLE_PROVEEDOR')")
@RequestMapping("/trabajo")
public class TrabajoControlador {
<<<<<<< Updated upstream
 
 @Autowired
 TrabajoServicio trabajoServicio;
 @Autowired
 ProveedorServicio proveedorServicio;
    
        @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN','ROLE_PROVEEDOR')")
@GetMapping("/contratar/{dni}")
public String contratar (@PathVariable String dni, ModelMap modelo, HttpSession session) {
=======
>>>>>>> Stashed changes

    @Autowired
    TrabajoServicio trabajoServicio;
    @Autowired
    ProveedorServicio proveedorServicio;
    @Autowired
    ComentarioServicio comentarioServicio;
    @Autowired
    ClienteServicio clienteServicio;

<<<<<<< Updated upstream
      
modelo.put("cliente", cliente);           
        
    modelo.addAttribute("proveedor",proveedor);
   
    return "registroTrabajo.html";
}

@PostMapping("/registro")
public String registro (String dniProveedor, String dniCliente, String detalleDeSolicitud ) {
    
    
     try {
         trabajoServicio.solicitudTrabajo(dniProveedor, dniCliente, detalleDeSolicitud);
     } catch (MiException ex) {
         return "registroTrabajo.html";
     }
   
    return "index.html";
}







   
    
    
    
=======
    //id trabajo, comentario, calificacion 
    @GetMapping("/comentar/{idTrabajo}")
    public String mostrarFormularioComentario(@PathVariable Long idTrabajo, ModelMap modelo) {
        //buen intento pero asi no es :)
        //  modelo.addAttribute("idTrabajo", idTrabajo);
        Trabajo trabajo = trabajoServicio.getOne(idTrabajo);
        modelo.addAttribute("trabajo", trabajo);

        
           String dniProveedor = trabajo.getProveedor().getDni();
        Proveedor proveedor = proveedorServicio.getOne(dniProveedor);
        String dniCliente = trabajo.getCliente().getDni();
        Cliente cliente = clienteServicio.getOne(dniCliente);
        modelo.put("cliente", cliente);
        modelo.addAttribute("proveedor", proveedor);
        
        
        return "comentarTrabajo.html";
    }

    //en este grupo no trabajamos con el requestparam (pedir explicacion al max)
    /*  @RequestParam String idComentario,
    EL IDCOMENTARIO ES AUTOSETEABLE
            @RequestParam Long idTrabajo,
            @RequestParam String contenido,
            @RequestParam Integer calificacion,
    
    EL BOOLEAN ALTA ES AUTOSETEABLE
            @RequestParam Boolean altaBaja){*/
    @PostMapping("/comentar")
    public String comentar(Long idTrabajo, String contenido, Integer calificacion) {
        try {
            comentarioServicio.CrearComentario(contenido, calificacion, idTrabajo);
        } catch (MiException ex) {
        }
        /*
        try {
            comentarioServicio.CrearComentario(idComentario, contenido, 0, LocalDateTime.MAX, true, idTrabajo);
             } catch (MiException ex) {                     
            return "comentarTrabajo.html";
        }
         */
        return "index1.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN','ROLE_PROVEEDOR')")
    @GetMapping("/contratar/{dni}")
    public String contratar(@PathVariable String dni, ModelMap modelo, HttpSession session) {

        Proveedor proveedor = proveedorServicio.getOne(dni);

        Persona cliente = (Persona) session.getAttribute("usuariosession");

        modelo.put("cliente", cliente);

        modelo.addAttribute("proveedor", proveedor);

        modelo.put("numero", 0);

        return "registroTrabajo.html";
    }

    @PostMapping("/registro")
    public String registro(String dniProveedor, String dniCliente, String detalleDeSolicitud) {

        try {
            trabajoServicio.solicitudTrabajo(dniProveedor, dniCliente, detalleDeSolicitud);
        } catch (MiException ex) {
            return "registroTrabajo.html";
        }

        return "index1.html";
    }

    @GetMapping("/presupuestar/{idTrabajo}")
    public String presupuestar(@PathVariable Long idTrabajo, ModelMap modelo) {

        Trabajo trabajo = trabajoServicio.getOne(idTrabajo);

        String dniProveedor = trabajo.getProveedor().getDni();

        Proveedor proveedor = proveedorServicio.getOne(dniProveedor);

        String dniCliente = trabajo.getCliente().getDni();

        Cliente cliente = clienteServicio.getOne(dniCliente);

        modelo.put("cliente", cliente);

        modelo.addAttribute("proveedor", proveedor);

        modelo.addAttribute("trabajo", trabajo);

        modelo.put("numero", 1);

        return "RegistroTrabajoProveedor.html";

    }

    @PostMapping("/presupuesto")
    public String presupuesto(Long idTrabajo, String respuestaProveedor,
            Integer GastosAdicionales, Integer horasTrabajadasEstimadas) {
        Boolean aceptacion = true;

        //esto de4spues se elimina
        Trabajo trabajo = trabajoServicio.getOne(idTrabajo);

        String dniProveedor = trabajo.getProveedor().getDni();
//hasta aqui

        trabajoServicio.revisionDeTrabajo(idTrabajo, aceptacion,
                respuestaProveedor, GastosAdicionales, horasTrabajadasEstimadas, dniProveedor);

        return "index1.html";
    }

    @GetMapping("/aceptar/{idTrabajo}")
    public String aceptar(@PathVariable Long idTrabajo, ModelMap modelo, HttpSession session) throws MiException {
        /*
          Trabajo trabajo = trabajoServicio.getOne(idTrabajo);
       String dniProveedor = trabajo.getProveedor().getDni();
        Proveedor proveedor = proveedorServicio.getOne(dniProveedor);
        String dniCliente = trabajo.getCliente().getDni();
        Cliente cliente = clienteServicio.getOne(dniCliente);
        modelo.put("cliente", cliente);
        modelo.addAttribute("proveedor", proveedor);
        modelo.addAttribute("trabajo", trabajo);
        modelo.put("numero", 1);
                return "AceptarTrabajoCliente.html";
         */
        Boolean aceptacion = true;

        trabajoServicio.aceptacionCliente(idTrabajo, aceptacion);

        return "index1.html";

    }

    @GetMapping("/rechazar/{idTrabajo}")
    public String rechazar(@PathVariable Long idTrabajo, ModelMap modelo, HttpSession session) throws MiException {
        Boolean aceptacion = false;
        trabajoServicio.aceptacionCliente(idTrabajo, aceptacion);
        return "index1.html";
    }

    @GetMapping("/finalizar/{idTrabajo}")
    public String finalizar(@PathVariable Long idTrabajo, ModelMap modelo, HttpSession session) throws MiException {
        Boolean aceptacion = true;
        //despues se elimina
        Trabajo trabajo = trabajoServicio.getOne(idTrabajo);
        String idProvedor = trabajo.getProveedor().getDni();
        //hast a aqui

        trabajoServicio.finalizadoTrabajo(idTrabajo, idProvedor, aceptacion);
        return "index1.html";
    }

    @GetMapping("/cancelaProveedor/{idTrabajo}")
    public String cancelaProveedor(@PathVariable Long idTrabajo, ModelMap modelo, HttpSession session) throws MiException {
        Boolean aceptacion = false;
        //despues se elimina
        Trabajo trabajo = trabajoServicio.getOne(idTrabajo);
        String idProvedor = trabajo.getProveedor().getDni();
        //hast a aqui

        trabajoServicio.finalizadoTrabajo(idTrabajo, idProvedor, aceptacion);
        return "index1.html";
    }

    @GetMapping("/listarProveedor/")
    public String listarProveedor(ModelMap modelo, HttpSession session) {

        Persona cliente = (Persona) session.getAttribute("usuariosession");

        String dniProveedor = cliente.getDni();

        List<Trabajo> trabajos = trabajoServicio.listarTrabajoProveedor(dniProveedor);

        //  List <Trabajo> trabajos = trabajoServicio.listarTodos();
        modelo.addAttribute("trabajos", trabajos);

        return "lista_trabajo_proveedor.html";
    }

>>>>>>> Stashed changes
}
/*
 public void revisionDeTrabajo(Long idtrabajo, Boolean aceptacion,
            String respuestaProveedor, int gastosAdicionales,
            int horasTrabajadasEstimadas, String dniProveedor) {
 */
