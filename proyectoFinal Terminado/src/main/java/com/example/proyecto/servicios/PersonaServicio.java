package com.example.proyecto.servicios;

import com.example.proyecto.entidades.Cliente;
import com.example.proyecto.entidades.Persona;
import com.example.proyecto.enumeraciones.Rol;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.repositorios.PersonaRepositorio;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class PersonaServicio implements UserDetailsService {//El llanero Solitario.

    @Autowired
    WhatsappServicio whatsappServicio;

    // por qu instanciamos en vez de usar el autowired?????
    private final PersonaRepositorio personaRepositorio;

    // @Autowired
    public PersonaServicio(PersonaRepositorio personaRepositorio) {
        this.personaRepositorio = personaRepositorio;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Persona persona = personaRepositorio.BuscarPorEmail(email);

        if (persona != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + persona.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);
// VICTOR !!! si por conveniencia venimos trabajando con "usuariosession", 
//no pongas personasession que daña todos los demas donde se lo llama
            session.setAttribute("usuariosession", persona);
            // session.setAttribute("personasession", persona);
            return new User(persona.getEmail(), persona.getPassword(), permisos);
        } else {
            return null;
        }
    }

    //gustavo
    @Transactional
    public List<Persona> listarPersonas() {

        List<Persona> persona = new ArrayList<>();

        persona = personaRepositorio.findAll();

        return persona;
    }

    public Persona getOne(String id) {
        return personaRepositorio.getOne(id);

    }

    public Persona buscarPorEmail(String email) {
        Persona persona = personaRepositorio.BuscarPorEmail(email);
        return persona;
    }

    @Transactional
    public String recuperarContraseña(String email) throws MiException {
        String respuesta = null;
        Persona persona = null;
        persona = personaRepositorio.BuscarPorEmail(email);

        if (persona == null) {
            throw new MiException("el email no se encuentra registrado");
        } else {
            Integer token1 = (int) (Math.random() * 1000000);
            String token = token1.toString();

            persona.setToken(token);
            personaRepositorio.save(persona);
            token = persona.getToken();
            token = "TU CLAVE PARA RECUPERAR LA CONTRASEÑA ES: " + token;

            respuesta = whatsappServicio.enviarMensaje(persona.getTelefono(), token);

        }
        return respuesta;

    }

    @Transactional
    public void actualizarContrasena(String email, String token, String password, String password2) throws MiException {

        Persona persona = null;
        persona = personaRepositorio.BuscarPorEmail(email);

        if (token.equalsIgnoreCase(persona.getToken()) && persona != null) {
            validar(password, password2);
            persona.setPassword(new BCryptPasswordEncoder().encode(password));
            persona.setToken(null);
            personaRepositorio.save(persona);
        } else {
            throw new MiException("EL TOKEN INGRESADO NO ES CORRECTO, REINTENTE");
        }

    }

    private void validar(String password, String password2) throws MiException {
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");

        }
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }
    }
    
    public void hacerAdmin(String dni) throws MiException{
    
        Optional<Persona> respuesta = personaRepositorio.findById(dni);
        if (respuesta.isPresent()) {
           Persona persona = respuesta.get();
           if(persona.getRol().equals("ADMIN")){
           throw new MiException("La Persona ya es Admin");
           }else{           
                   persona.setRol(Rol.ADMIN);           
           personaRepositorio.save(persona);
           }
        }        
    }

}
