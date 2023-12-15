
package com.example.proyecto.repositorios;


import com.example.proyecto.entidades.Proveedor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepositorio extends JpaRepository<Proveedor, String> {

    @Query("SELECT u FROM Proveedor u WHERE u.dni = :dni")
    public Proveedor buscarPorDni(@Param("dni") String dni);

    @Query("SELECT p FROM Proveedor p WHERE "
            + "CONCAT(p.nombre, p.apellido, p.domicilio.rubro) "
            + "LIKE %?1%")
    List<Proveedor> findAll(String palabraClave);

    boolean existsByDni(String dni);

    boolean existsByEmail(String email);
    
    
    @Query("SELECT p FROM Proveedor p WHERE p.email = :email")
    public Proveedor BuscarPorEmail(@Param("email") String email);
    

}
