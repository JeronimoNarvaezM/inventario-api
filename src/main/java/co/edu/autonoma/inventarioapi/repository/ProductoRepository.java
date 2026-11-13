package co.edu.autonoma.inventarioapi.repository;

import co.edu.autonoma.inventarioapi.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository
        extends JpaRepository<ProductoEntity, Long> {

    // Devuelve true si existe al menos un producto en esa categoría
    boolean existsByCategoria_Id(Long categoriaId);
}