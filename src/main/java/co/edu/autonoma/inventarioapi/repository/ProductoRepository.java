
package co.edu.autonoma.inventarioapi.repository;

import co.edu.autonoma.inventarioapi.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {
}
