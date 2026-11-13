package co.edu.autonoma.inventarioapi.service;

import co.edu.autonoma.inventarioapi.dto.EstadoProductoRequest;
import co.edu.autonoma.inventarioapi.dto.ProductoRequest;
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

    @Transactional(readOnly = true)
    public ProductoResponse obtenerProducto(Long id) {
        return convertirAResponse(buscarProducto(id));
    }

    @Transactional
    public ProductoResponse crearProducto(ProductoRequest request) {
        validarDatosProducto(
                request == null ? null : request.nombre(),
                request == null ? null : request.umbralAlerta(),
                request == null ? null : request.categoriaId()
        );

        CategoriaEntity categoria = buscarCategoria(request.categoriaId());

        ProductoEntity producto = new ProductoEntity();
        producto.setNombre(request.nombre().trim());
        producto.setDescripcion(request.descripcion());
        producto.setUmbralAlerta(request.umbralAlerta());
        producto.setCategoria(categoria);
        // stockActual = 0 y activo = true ya vienen por defecto en la entidad.
        // El stock NO se envía al crear: solo cambia con movimientos (RN-03).

        return convertirAResponse(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponse actualizarProducto(
            Long id, ProductoUpdateRequest request) {

        validarDatosProducto(
                request == null ? null : request.nombre(),
                request == null ? null : request.umbralAlerta(),
                request == null ? null : request.categoriaId()
        );

        ProductoEntity producto = buscarProducto(id);
        CategoriaEntity categoria = buscarCategoria(request.categoriaId());

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

        ProductoEntity producto = buscarProducto(id);
        producto.setActivo(request.activo());

        return convertirAResponse(productoRepository.save(producto));
    }

    private ProductoEntity buscarProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    private CategoriaEntity buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoría no encontrada"));
    }

    // Validación compartida por crear y actualizar (incluye RN-04)
    private void validarDatosProducto(
            String nombre, Integer umbralAlerta, Long categoriaId) {

        if (nombre == null
                || nombre.isBlank()
                || umbralAlerta == null
                || umbralAlerta < 0
                || categoriaId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre, el umbral no negativo y la categoría son obligatorios"
            );
        }

        // La columna en la BD acepta máximo 150 caracteres
        if (nombre.trim().length() > 150) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre del producto no puede superar 150 caracteres"
            );
        }
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