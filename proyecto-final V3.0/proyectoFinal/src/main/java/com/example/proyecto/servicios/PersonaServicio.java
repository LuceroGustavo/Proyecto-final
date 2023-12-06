package com.example.proyecto.servicios;

import com.example.proyecto.entidades.Cliente;
import com.example.proyecto.entidades.Persona;
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

@Service
public class PersonaServicio implements UserDetailsService {//El llanero Solitario.

   


    private final PersonaRepositorio personaRepositorio;

   // @Autowired
    public PersonaServicio(PersonaRepositorio personaRepositorio) {
        this.personaRepositorio = personaRepositorio;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        //se cambio por persona
        //   y se implemento repositorio de persona
        Persona persona =personaRepositorio.BuscarPorEmail(email);

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

    public Persona getOne(String id){
        return personaRepositorio.getOne(id);

}


    

}



