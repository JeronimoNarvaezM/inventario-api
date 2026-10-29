
package co.edu.autonoma.inventarioapi.service;

import co.edu.autonoma.inventarioapi.dto.EstadoProductoRequest;
import co.edu.autonoma.inventarioapi.dto.ProductoResponse;
import co.edu.autonoma.inventarioapi.dto.ProductoUpdateRequest;
import co.edu.autonoma.inventarioapi.entity.CategoriaEntity;
import co.edu.autonoma.inventarioapi.entity.ProductoEntity;
import co.edu.autonoma.inventarioapi.repository.CategoriaRepository;
import co.edu.autonoma.inventarioapi.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarProductos() {
        return productoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional
    public ProductoResponse actualizarProducto(
            Long id, ProductoUpdateRequest request) {

        if (request == null
                || request.nombre() == null
                || request.nombre().isBlank()
                || request.umbralAlerta() == null
                || request.umbralAlerta() < 0
                || request.categoriaId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre, el umbral no negativo y la categoría son obligatorios"
            );
        }

        ProductoEntity producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado"));

        CategoriaEntity categoria = categoriaRepository
                .findById(request.categoriaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoría no encontrada"));

        producto.setNombre(request.nombre().trim());
        producto.setDescripcion(request.descripcion());
        producto.setUmbralAlerta(request.umbralAlerta());
        producto.setCategoria(categoria);

        return convertirAResponse(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponse cambiarEstado(
            Long id, EstadoProductoRequest request) {

        if (request == null || request.activo() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debes indicar si el producto está activo"
            );
        }

        ProductoEntity producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado"));

        producto.setActivo(request.activo());

        return convertirAResponse(productoRepository.save(producto));
    }

    private ProductoResponse convertirAResponse(ProductoEntity producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getStockActual(),
                producto.getUmbralAlerta(),
                producto.isActivo(),
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre()
        );
    }
}
