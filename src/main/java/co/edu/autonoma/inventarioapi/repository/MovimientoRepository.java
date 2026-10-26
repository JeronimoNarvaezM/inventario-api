package co.edu.autonoma.inventarioapi.repository;

import co.edu.autonoma.inventarioapi.entity.MovimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoRepository
        extends JpaRepository<MovimientoEntity, Long> {

    List<MovimientoEntity> findByProducto_IdOrderByFechaDesc(
            Long productoId);
}
