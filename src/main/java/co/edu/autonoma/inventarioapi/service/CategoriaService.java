package co.edu.autonoma.inventarioapi.service;

import co.edu.autonoma.inventarioapi.dto.CategoriaRequest;
import co.edu.autonoma.inventarioapi.dto.CategoriaResponse;
import co.edu.autonoma.inventarioapi.dto.CategoriaUpdateRequest;
import co.edu.autonoma.inventarioapi.entity.CategoriaEntity;
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
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarCategorias() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse obtenerCategoria(Long id) {
        return convertirAResponse(buscarCategoria(id));
    }

    @Transactional
    public CategoriaResponse crearCategoria(CategoriaRequest request) {
        validarNombre(request == null ? null : request.nombre());

        String nombre = request.nombre().trim();

        if (categoriaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una categoría con ese nombre"
            );
        }

        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setNombre(nombre);
        categoria.setDescripcion(request.descripcion());

        return convertirAResponse(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaResponse actualizarCategoria(
            Long id, CategoriaUpdateRequest request) {

        validarNombre(request == null ? null : request.nombre());

        CategoriaEntity categoria = buscarCategoria(id);
        String nombre = request.nombre().trim();

        // Solo revisamos duplicados si el nombre realmente cambió
        boolean cambioElNombre = !categoria.getNombre().equalsIgnoreCase(nombre);
        if (cambioElNombre && categoriaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una categoría con ese nombre"
            );
        }

        categoria.setNombre(nombre);
        categoria.setDescripcion(request.descripcion());

        return convertirAResponse(categoriaRepository.save(categoria));
    }

    @Transactional
    public void eliminarCategoria(Long id) {
        CategoriaEntity categoria = buscarCategoria(id);

        if (productoRepository.existsByCategoria_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar la categoría porque tiene productos asociados"
            );
        }

        categoriaRepository.delete(categoria);
    }

    private CategoriaEntity buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoría no encontrada"));
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre de la categoría es obligatorio"
            );
        }

        // La columna en la BD acepta máximo 100 caracteres
        if (nombre.trim().length() > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre de la categoría no puede superar 100 caracteres"
            );
        }
    }

    private CategoriaResponse convertirAResponse(CategoriaEntity categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );
    }
}