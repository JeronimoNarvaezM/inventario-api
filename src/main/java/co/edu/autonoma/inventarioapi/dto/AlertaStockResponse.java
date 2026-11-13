package co.edu.autonoma.inventarioapi.dto;

import java.time.LocalDateTime;

public record AlertaStockResponse(
        Long id,
        LocalDateTime fecha,
        Long productoId,
        String productoNombre,
        Integer stockActual,
        Integer umbral,
        String mensaje
) {
}