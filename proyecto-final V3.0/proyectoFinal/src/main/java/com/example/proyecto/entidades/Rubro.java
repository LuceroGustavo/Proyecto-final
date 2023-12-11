
package com.example.proyecto.entidades;

//import com.example.proyecto.enumeraciones.TipoRubro;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
public class Rubro {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String idRubro;
    private String descripcion;//Detalle de los trabajo
    private String nombreRubro;
   

    
    
    
}
