package co.edu.autonoma.inventarioapi.controller;

import co.edu.autonoma.inventarioapi.dto.UsuarioRequest;
import co.edu.autonoma.inventarioapi.dto.UsuarioResponse;
import co.edu.autonoma.inventarioapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioResponse> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    public UsuarioResponse obtenerUsuario(@PathVariable Long id) {
        return usuarioService.obtenerUsuario(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse crearUsuario(@RequestBody UsuarioRequest request) {
        return usuarioService.crearUsuario(request);
    }
}