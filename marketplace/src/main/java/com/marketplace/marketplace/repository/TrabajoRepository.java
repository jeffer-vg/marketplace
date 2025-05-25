package com.marketplace.marketplace.repository;

import com.marketplace.marketplace.model.Trabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TrabajoRepository extends JpaRepository<Trabajo, Long> {
    List<Trabajo> findByCategoria(String categoria);
    List<Trabajo> findByAprobadoTrue();  // Trabajos aprobados
    List<Trabajo> findByAprobadoFalse(); // Trabajos no aprobados
    
}
