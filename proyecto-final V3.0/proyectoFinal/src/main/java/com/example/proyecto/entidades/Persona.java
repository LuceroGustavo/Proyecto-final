
package com.example.proyecto.entidades;

import com.example.proyecto.enumeraciones.Rol;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
public class Persona {
    /*
    @GeneratedValue(strategy = GenerationType.IDENTITY) especifica que el valor de id se generará
    automáticamente por la base de datos utilizando una estrategia de identidad (autoincremental).
    */
  @Id
  private String dni;
  private String nombre;
  private String apellido;
  private String domicilio;
  private String telefono;
  private String email;
  private String password;
  private String token;
  
  @Temporal(TemporalType.DATE)
  private Date fechaAlta; 
  private boolean alta;
  @OneToOne
  private Imagen imagen;

    @Enumerated(EnumType.STRING)
    private Rol rol;
    
   
  
   
}
