
package co.edu.autonoma.inventarioapi.controller;

import co.edu.autonoma.inventarioapi.dto.EstadoProductoRequest;
import co.edu.autonoma.inventarioapi.dto.ProductoResponse;
import co.edu.autonoma.inventarioapi.dto.ProductoUpdateRequest;
import co.edu.autonoma.inventarioapi.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public List<ProductoResponse> listarProductos() {
        return productoService.listarProductos();
    }

    @PutMapping("/{id}")
    public ProductoResponse actualizarProducto(
            @PathVariable Long id,
            @RequestBody ProductoUpdateRequest request) {
        return productoService.actualizarProducto(id, request);
    }

    @PatchMapping("/{id}/estado")
    public ProductoResponse cambiarEstado(
            @PathVariable Long id,
            @RequestBody EstadoProductoRequest request) {
        return productoService.cambiarEstado(id, request);
    }
}
