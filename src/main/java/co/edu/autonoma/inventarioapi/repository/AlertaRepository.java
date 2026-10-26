package co.edu.autonoma.inventarioapi.repository;

import co.edu.autonoma.inventarioapi.entity.AlertaStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertaRepository
        extends JpaRepository<AlertaStockEntity, Long> {
}