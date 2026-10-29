
package co.edu.autonoma.inventarioapi.dto;

import co.edu.autonoma.inventarioapi.entity.TipoMovimiento;

public record MovimientoRequest(
        TipoMovimiento tipo,
        Integer cantidad,
        Long usuarioId,
        String observacion,
        Long productoId
) {
}
