
package co.edu.autonoma.inventarioapi.dto;

public record ProductoRequest(
        String nombre,
        String descripcion,
        Integer umbralAlerta,
        Long categoriaId
) {
}
