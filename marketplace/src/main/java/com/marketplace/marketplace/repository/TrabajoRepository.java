package com.marketplace.marketplace.repository;

import com.marketplace.marketplace.model.Trabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrabajoRepository extends JpaRepository<Trabajo, Long> {
    List<Trabajo> findByAprobadoTrue();  // Trabajos aprobados
@Query("SELECT t FROM Trabajo t WHERE t.aprobado = true AND (LOWER(t.titulo) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(t.descripcion) LIKE LOWER(CONCAT('%', :query, '%')))")
List<Trabajo> buscarPorTituloODescripcion(@Param("query") String query);
    List<Trabajo> findByCategoria(String categoria);

    List<Trabajo> findByAprobadoFalse(); // Trabajos no aprobados
    
}
