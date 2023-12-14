package com.example.proyecto.entidades;

import com.example.proyecto.enumeraciones.Rol;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import lombok.Data;
//ProyectoSubido


//// ESTO SE USA?????????????????
@Data
@Entity
@PrimaryKeyJoinColumn(name = "persona_dni")
public class Administrador extends Persona {

    // Otros campos y anotaciones
    @ManyToMany
    @JoinTable(
            name = "administrador_servicios",
            joinColumns = @JoinColumn(name = "administrador_id"),
            inverseJoinColumns = @JoinColumn(name = "servicio_id"))
    private Set<Trabajo> servicios;

    @ElementCollection(targetClass = Rol.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "administrador_roles", joinColumns = @JoinColumn(name = "administrador_id"))
    @Column(name = "rol")
    private Set<Rol> roles;

    // Getters y setters
}
