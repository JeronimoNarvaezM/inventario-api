
package co.edu.autonoma.inventarioapi.service;

import co.edu.autonoma.inventarioapi.entity.CategoriaEntity;
import co.edu.autonoma.inventarioapi.entity.ProductoEntity;
import co.edu.autonoma.inventarioapi.repository.CategoriaRepository;
import co.edu.autonoma.inventarioapi.repository.ProductoRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void obtenerProducto_cuandoExiste_devuelveProducto() {

        // Arrange
        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setNombre("Tecnologia");

        ProductoEntity producto = new ProductoEntity();
        producto.setNombre("Teclado");
        producto.setDescripcion("Teclado mecanico");
        producto.setStockActual(10);
        producto.setUmbralAlerta(5);
        producto.setActivo(true);
        producto.setCategoria(categoria);

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(producto));

        // Act
        var resultado = productoService.obtenerProducto(1L);

        // Assert
        assertThat(resultado).isNotNull();
        verify(productoRepository).findById(1L);
    }

    @Test
    void obtenerProducto_cuandoNoExiste_lanzaError404() {

        // Arrange
        when(productoRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act y Assert
        assertThatThrownBy(() -> productoService.obtenerProducto(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Producto no encontrado");

        verify(productoRepository).findById(99L);
    }
    @Test
    void crearProducto_conNombreVacio_lanzaError400() {

        // Arrange
        var request = new co.edu.autonoma.inventarioapi.dto.ProductoRequest(
                "   ",
                "Teclado mecanico",
                5,
                1L
        );

        // Act y Assert
        assertThatThrownBy(() -> productoService.crearProducto(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("obligatorios");

        // Verificar que no se guardó ningún producto
        verify(productoRepository, org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void cambiarEstado_cuandoDatosSonValidos_actualizaProducto() {

        // Arrange
        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setId(1L);
        categoria.setNombre("Tecnologia");

        ProductoEntity producto = new ProductoEntity();
        producto.setId(1L);
        producto.setNombre("Teclado");
        producto.setDescripcion("Teclado mecanico");
        producto.setStockActual(10);
        producto.setUmbralAlerta(5);
        producto.setActivo(true);
        producto.setCategoria(categoria);

        var request = new co.edu.autonoma.inventarioapi.dto.EstadoProductoRequest(false);

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(producto));

        when(productoRepository.save(producto))
                .thenReturn(producto);

        // Act
        var resultado = productoService.cambiarEstado(1L, request);

        // Assert
        assertThat(resultado.activo()).isFalse();

        verify(productoRepository).findById(1L);
        verify(productoRepository).save(producto);
    }

}
