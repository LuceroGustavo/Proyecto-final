package com.example.proyecto.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.proyecto.entidades.Trabajo;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface TrabajoRepositorio extends JpaRepository<Trabajo, Long>{

   @Query("SELECT t FROM Trabajo t JOIN t.cliente c WHERE c.dni = :dni")
   List<Trabajo> findByClienteDni(@Param("dni") String dni);
   
     @Query("SELECT t FROM Trabajo t JOIN t.proveedor t WHERE t.dni = :dni")
   List<Trabajo> findByProveedorDni(@Param("dni") String dni);
   
   
  
  
}


