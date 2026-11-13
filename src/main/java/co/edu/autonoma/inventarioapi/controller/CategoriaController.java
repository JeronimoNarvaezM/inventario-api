package co.edu.autonoma.inventarioapi.controller;

import co.edu.autonoma.inventarioapi.dto.CategoriaRequest;
import co.edu.autonoma.inventarioapi.dto.CategoriaResponse;
import co.edu.autonoma.inventarioapi.dto.CategoriaUpdateRequest;
import co.edu.autonoma.inventarioapi.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public List<CategoriaResponse> listarCategorias() {
        return categoriaService.listarCategorias();
    }

    @GetMapping("/{id}")
    public CategoriaResponse obtenerCategoria(@PathVariable Long id) {
        return categoriaService.obtenerCategoria(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponse crearCategoria(
            @RequestBody CategoriaRequest request) {
        return categoriaService.crearCategoria(request);
    }

    @PutMapping("/{id}")
    public CategoriaResponse actualizarCategoria(
            @PathVariable Long id,
            @RequestBody CategoriaUpdateRequest request) {
        return categoriaService.actualizarCategoria(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
    }
}