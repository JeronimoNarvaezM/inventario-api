package co.edu.autonoma.inventarioapi.repository;

import co.edu.autonoma.inventarioapi.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository
        extends JpaRepository<CategoriaEntity, Long> {

    Optional<CategoriaEntity> findByNombre(String nombre);

    // Para no permitir dos categorías con el mismo nombre
    boolean existsByNombreIgnoreCase(String nombre);
}