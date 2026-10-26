
package co.edu.autonoma.inventarioapi.dto;

public record ProductoUpdateRequest(
        String nombre,
        String descripcion,
        Integer umbralAlerta,
        Long categoriaId
) {
}
