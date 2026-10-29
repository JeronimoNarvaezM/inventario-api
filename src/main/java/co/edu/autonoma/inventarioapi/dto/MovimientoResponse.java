
package co.edu.autonoma.inventarioapi.dto;

import co.edu.autonoma.inventarioapi.entity.TipoMovimiento;
import java.time.LocalDateTime;

public record MovimientoResponse(
        Long id,
        TipoMovimiento tipo,
        Integer cantidad,
        LocalDateTime fecha,
        Long usuarioId,
        String usuarioNombre,
        String observacion,
        Long productoId,
        String productoNombre
) {
}
