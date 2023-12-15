
package com.example.proyecto.entidades;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import lombok.Data;

@Data
@Entity
public class Proveedor extends Persona{
    
private String matricula;//
private String descripcion;//
private int puntuacionPromedio;//esto es para las estrellitas

@OneToOne
private Rubro rubro;
private boolean estadoActual;//Depende del estado actual


private Double precioHora;
@Temporal(javax.persistence.TemporalType.DATE)
private Date fechaAlta;

///USAR CLASES PADRE!!!!!!!!!!!!!
//private int contdTrabajoRealizado;//esto permite scar un promedio de los trabajos realizados
private Integer contdTrabajoRealizado;

 @OneToOne
 private Imagen imagen;
 
}
