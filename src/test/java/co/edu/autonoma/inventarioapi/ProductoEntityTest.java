
package co.edu.autonoma.inventarioapi;

import co.edu.autonoma.inventarioapi.entity.ProductoEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductoEntityTest {

    @Test
    void crearProducto_conDatosIniciales_guardaLosValores() {

        // Arrange: preparar el producto
        ProductoEntity producto = new ProductoEntity();
        producto.setNombre("Teclado");
        producto.setDescripcion("Teclado mecanico");
        producto.setStockActual(10);

        // Act: consultar los valores
        String nombre = producto.getNombre();
        Integer stock = producto.getStockActual();

        // Assert: comprobar los resultados
        assertThat(nombre).isEqualTo("Teclado");
        assertThat(stock).isEqualTo(10);
    }
}
