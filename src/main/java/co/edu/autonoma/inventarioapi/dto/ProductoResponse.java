
package co.edu.autonoma.inventarioapi.dto;

public record ProductoResponse(
        Long id,
        String nombre,
        String descripcion,
        Integer stockActual,
        Integer umbralAlerta,
        boolean activo,
        Long categoriaId,
        String categoriaNombre
) {
}
