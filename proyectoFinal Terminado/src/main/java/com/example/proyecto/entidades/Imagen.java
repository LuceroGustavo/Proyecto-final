/*
GUSTAVVO 22/11
 */
package com.example.proyecto.entidades;

import com.example.proyecto.enumeraciones.Rol;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class Imagen {
     @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private String mime;
    
    private String nombre;
    
    //  @Column(columnDefinition = "LONGBLOB", length = 100000)
    @Lob @Basic(fetch = FetchType.LAZY)
    private byte[] contenido;

    //ESTO QUE HACE???????????
    @Enumerated(EnumType.STRING)
    private Rol rol;
    
//EL RUBRO NO DEBERIA ESTAR EN IMAGEN???
    /*
    @OneToOne
    @JoinColumn(name = "rubro_id")
    private Rubro rubro;
    */
}
