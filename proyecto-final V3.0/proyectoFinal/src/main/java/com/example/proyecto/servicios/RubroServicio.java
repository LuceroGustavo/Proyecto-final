/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.proyecto.servicios;

import com.example.proyecto.entidades.Rubro;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.repositorios.RubroRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author aprig
 */
@Service
public class RubroServicio {

    @Autowired
    RubroRepositorio rubroRepositorio;

    @Transactional
    public void CrearRubro(String nombreRubro, String descripcion) throws MiException {
        // recordar que el id rubro es autogenerable

        ValidarDatos(nombreRubro, descripcion);
        Rubro rubro = new Rubro();

        rubro.setNombreRubro(nombreRubro);
        rubro.setDescripcion(descripcion);

        rubroRepositorio.save(rubro);

    }

    @Transactional
    public List<Rubro> ListaRubros() {

        List<Rubro> rubros = new ArrayList<>();

        rubros = rubroRepositorio.findAll();

        return rubros;
    }

    @Transactional
    public void ModificarRubro(String IdRubro, String descripcion, String nombreRubro) throws MiException {
        ValidarDatos(nombreRubro, descripcion);
        // recordar que el id rubro es autogenerable
        // ValidarDatos(nombreRubro,idRubro);
        // faltan exepciones
        Optional<Rubro> respuesta = rubroRepositorio.findById(IdRubro);

        if (respuesta.isPresent()) {

            Rubro rubro = respuesta.get();

            rubro.setNombreRubro(nombreRubro);
            rubro.setDescripcion(descripcion);

            rubroRepositorio.save(rubro);

        }

    }

    @Transactional
    private void ValidarDatos(String nombreRubro, String descripcion) throws MiException {
        if (nombreRubro == null || nombreRubro.isEmpty()) {
            throw new MiException(" El nombre no puede ser nulo y tampoco estar vacio");

        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new MiException(" La descripcion no puede estar vacia, por favor explicar qeu hace este rubro.");
        }
        
        List<Rubro> rubros = new ArrayList<>();
        rubros = ListaRubros();

        for (Rubro rubro : rubros) {
            if (rubro.getNombreRubro().equalsIgnoreCase(nombreRubro)) {
                throw new MiException("El rubro ya se encuentra presente en la lista de rubros");

            }

        }

    }

}
