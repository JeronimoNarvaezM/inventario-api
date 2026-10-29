
package co.edu.autonoma.inventarioapi.controller;

import co.edu.autonoma.inventarioapi.dto.MovimientoRequest;
import co.edu.autonoma.inventarioapi.dto.MovimientoResponse;
import co.edu.autonoma.inventarioapi.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @GetMapping
    public List<MovimientoResponse> listarPorProducto(
            @RequestParam Long productoId) {
        return movimientoService.listarPorProducto(productoId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoResponse registrarMovimiento(
            @RequestBody MovimientoRequest request) {
        return movimientoService.registrarMovimiento(request);
    }
}
