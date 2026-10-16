
package co.edu.autonoma.inventarioapi.config;

import co.edu.autonoma.inventarioapi.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PersistenciaCheck implements CommandLineRunner {

    private final ProductoRepository repository;

    public PersistenciaCheck(ProductoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        System.out.println(
                "Total: " + repository.count()
        );
    }
}
