package com.example.proyecto.servicios;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import com.example.proyecto.entidades.Persona;
import com.example.proyecto.enumeraciones.Rol;
import com.example.proyecto.repositorios.PersonaRepositorio;

@Service
public class AdministradorServicio { //GUSTAVO - METODO RECIBE EL ID DE PERSONA MAS EL ROL A
                                     //MODIFICAR  Y GUARDA EN REPOSITORIO (VALIDA SI YA LO TIENE)
    @Autowired
    PersonaRepositorio personaRepositorio;
 
    @Transactional
    @Secured("ROLE_ADMIN")
    public void cambiarRol(String id, Rol nuevoRol) {
        Optional<Persona> respuesta = personaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Persona persona = respuesta.get();

            if (persona.getRol() != nuevoRol) { // Validar si el nuevo rol es diferente al rol actual
                persona.setRol(nuevoRol);
                personaRepositorio.save(persona);
            } else {
                // El nuevo rol es el mismo que el rol actual, puedes lanzar una excepción, imprimir un mensaje o manejarlo según tus necesidades
                throw new IllegalArgumentException("El usuario ya tiene asignado el rol que estás intentando asignar.");
            }
        }
    }
}