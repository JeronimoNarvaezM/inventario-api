
package co.edu.autonoma.inventarioapi.dto;

import co.edu.autonoma.inventarioapi.entity.Rol;

public record UsuarioRequest(
        String nombre,
        String correo,
        Rol rol
) {
}
