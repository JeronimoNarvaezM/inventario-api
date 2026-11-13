package co.edu.autonoma.inventarioapi.controller;

import co.edu.autonoma.inventarioapi.dto.AlertaStockResponse;
import co.edu.autonoma.inventarioapi.service.AlertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
public class AlertaController {

    private final AlertaService alertaService;

    // productoId es opcional: sin él devuelve todas las alertas
    @GetMapping
    public List<AlertaStockResponse> listarAlertas(
            @RequestParam(required = false) Long productoId) {
        return alertaService.listarAlertas(productoId);
    }
}