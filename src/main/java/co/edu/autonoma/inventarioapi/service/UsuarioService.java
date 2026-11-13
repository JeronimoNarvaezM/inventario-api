package co.edu.autonoma.inventarioapi.service;

import co.edu.autonoma.inventarioapi.dto.UsuarioRequest;
import co.edu.autonoma.inventarioapi.dto.UsuarioResponse;
import co.edu.autonoma.inventarioapi.entity.UsuarioEntity;
import co.edu.autonoma.inventarioapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerUsuario(Long id) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        return convertirAResponse(usuario);
    }

    @Transactional
    public UsuarioResponse crearUsuario(UsuarioRequest request) {
        validarDatosUsuario(request);

        String nombre = request.nombre().trim();
        String correo = request.correo().trim().toLowerCase();

        if (usuarioRepository.existsByCorreoIgnoreCase(correo)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un usuario con ese correo"
            );
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setRol(request.rol());
        // activo = true viene por defecto en la entidad

        return convertirAResponse(usuarioRepository.save(usuario));
    }

    private void validarDatosUsuario(UsuarioRequest request) {
        if (request == null
                || request.nombre() == null
                || request.nombre().isBlank()
                || request.correo() == null
                || request.correo().isBlank()
                || request.rol() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre, el correo y el rol son obligatorios"
            );
        }

        if (!request.correo().contains("@")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El correo no tiene un formato válido"
            );
        }

        // Las columnas en la BD aceptan máximo 150 caracteres
        if (request.nombre().trim().length() > 150
                || request.correo().trim().length() > 150) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre y el correo no pueden superar 150 caracteres"
            );
        }
    }

    private UsuarioResponse convertirAResponse(UsuarioEntity usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol(),
                usuario.isActivo()
        );
    }
}