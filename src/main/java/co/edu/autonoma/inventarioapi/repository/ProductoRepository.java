package co.edu.autonoma.inventarioapi.repository;

import co.edu.autonoma.inventarioapi.entity.ProductoEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductoRepository
        extends JpaRepository<ProductoEntity, Long> {

    // Devuelve true si existe al menos un producto en esa categoría
    boolean existsByCategoria_Id(Long categoriaId);

    // Igual que findById, pero bloquea la fila del producto
    // hasta que termine la transacción
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from ProductoEntity p where p.id = :id")
    Optional<ProductoEntity> buscarParaActualizar(@Param("id") Long id);
}