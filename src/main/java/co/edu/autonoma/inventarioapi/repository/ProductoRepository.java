
package co.edu.autonoma.inventarioapi.repository;

import co.edu.autonoma.inventarioapi.domain.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository
        extends JpaRepository<ProductoEntity, Long> {
}
