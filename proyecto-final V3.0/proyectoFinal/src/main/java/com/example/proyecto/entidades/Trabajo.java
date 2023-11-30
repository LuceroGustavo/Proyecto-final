
package com.example.proyecto.entidades;

import com.example.proyecto.enumeraciones.EstadoTrabajo;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
public class Trabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date fechaInicio;

    @Temporal(TemporalType.DATE)
    private Date fechaFinalizado;

    private int puntuacionTrabajo; // del 1 al 5
    private EstadoTrabajo estadoTrabajo;
    private String cometarioTrabajoTerminado;
    private String detalleDeSolicitud;
    private String observacionCancelado;
    private int horasTrabajoEstimadas;// Lo completa el provedor x horas de trabajo
    private int gastosAdicionales;
    private double precioFinal;// este campo se completa con la horas trabajas x precio hora trabajada x el
                            // provedor
    private String respuestaProveedor; // el proovedor comenta el trabajo que va a realizar
    @OneToOne
    private Proveedor proveedor;
    @OneToOne
    private Cliente cliente;
}

// recordar Escribir las Tarjetas de servicios a ServicioServicios.
