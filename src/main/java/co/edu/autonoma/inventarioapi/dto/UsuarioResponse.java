
package co.edu.autonoma.inventarioapi.dto;

import co.edu.autonoma.inventarioapi.entity.Rol;

public record UsuarioResponse(
        Long id,
        String nombre,
        String correo,
        Rol rol,
        boolean activo
) {
}
