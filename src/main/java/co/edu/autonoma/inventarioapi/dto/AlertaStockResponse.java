
package co.edu.autonoma.inventarioapi.dto;

import java.time.LocalDateTime;

public record AlertaStockResponse(
        Long id,
        LocalDateTime fecha,
        Integer stockActual,
        Integer umbral,
        String mensaje
) {
}
