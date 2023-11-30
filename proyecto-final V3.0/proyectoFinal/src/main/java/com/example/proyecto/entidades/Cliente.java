
 package com.example.proyecto.entidades;



import com.example.proyecto.enumeraciones.Rol;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Data;


@Data
@Entity
public class Cliente extends Persona{

  @Enumerated(EnumType.STRING)
    private Rol rol;
  
    
}
