package co.edu.autonoma.inventarioapi.repository;

import co.edu.autonoma.inventarioapi.entity.AlertaStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertaRepository
        extends JpaRepository<AlertaStockEntity, Long> {

    // Todas las alertas, de la más reciente a la más antigua
    List<AlertaStockEntity> findAllByOrderByFechaDesc();

    // Alertas de un producto, de la más reciente a la más antigua
    List<AlertaStockEntity> findByProducto_IdOrderByFechaDesc(Long productoId);
}