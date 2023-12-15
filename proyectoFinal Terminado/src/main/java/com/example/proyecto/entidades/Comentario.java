package com.example.proyecto.entidades;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Comentario {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String contenido;
    private Integer calificacion;

    @Temporal(TemporalType.DATE)
    private Date fechaHora;
    //este se los cambie por que no se como se usa
    //  private LocalDateTime fechaHora;  // Agrega la fecha y hora al comentario
    private boolean altaBaja;

    //tambien le agregue el atributo Trabajo que esta en la lista de tareas
    @OneToOne
    private Trabajo trabajo;

    /*
      public Comentario(String id, String contenido, Integer calificacion, LocalDateTime fechaHora, boolean altaBaja) {
        this.id = id;
     */
    public Comentario() {
    }

    public Comentario(String contenido, Integer calificacion, Date fechaHora, boolean altaBaja) {

        this.contenido = contenido;
        this.calificacion = calificacion;
        this.fechaHora = fechaHora;
        this.altaBaja = altaBaja;
    }

    // Métodos getter para la fecha y hora
    public Date getFechaHora() {
        return fechaHora;
    }

    public String getContenido() {
        return contenido;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    /*  
    Con esta modificación, cada vez que se crea un nuevo comentario, se registra la fecha y hora actual. Luego, puedes acceder a esta información a través del método getFechaHora().

Por ejemplo, al imprimir los comentarios en el código principal, podrías mostrar la fecha y hora junto con el contenido y la calificación:
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAltaBaja() {
        return altaBaja;
    }

    public void setAltaBaja(boolean altaBaja) {
        this.altaBaja = altaBaja;
    }

    public Trabajo getTrabajo() {
        return trabajo;
    }

    public void setTrabajo(Trabajo trabajo) {
        this.trabajo = trabajo;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

}
