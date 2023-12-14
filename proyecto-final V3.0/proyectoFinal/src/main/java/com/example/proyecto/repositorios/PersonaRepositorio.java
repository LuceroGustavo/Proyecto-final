
package com.example.proyecto.repositorios;

import com.example.proyecto.entidades.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface PersonaRepositorio extends JpaRepository<Persona, String>{
    
    @Query("SELECT p FROM Persona p WHERE p.email = :email")
    public Persona BuscarPorEmail(@Param("email") String email);
   
}
//agregar queri que se logueen los activos
// listar por email y los activos en misma query