package co.edu.autonoma.inventarioapi.service;

import co.edu.autonoma.inventarioapi.dto.AlertaStockResponse;
import co.edu.autonoma.inventarioapi.entity.AlertaStockEntity;
import co.edu.autonoma.inventarioapi.entity.ProductoEntity;
import co.edu.autonoma.inventarioapi.repository.AlertaRepository;
import co.edu.autonoma.inventarioapi.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertaService {

    private final AlertaRepository alertaRepository;
    private final ProductoRepository productoRepository;

    // Crea una alerta si el stock quedó POR DEBAJO del umbral
    @Transactional
    public void evaluarStock(ProductoEntity producto) {
        int stock = producto.getStockActual();
        int umbral = producto.getUmbralAlerta();

        if (stock < umbral) {
            AlertaStockEntity alerta = new AlertaStockEntity();
            alerta.setProducto(producto);
            alerta.setStockActual(stock);
            alerta.setUmbral(umbral);
            alerta.setMensaje(
                    "El producto '" + producto.getNombre()
                            + "' tiene stock " + stock
                            + ", por debajo del umbral " + umbral
            );

            alertaRepository.save(alerta);
        }
    }

    @Transactional(readOnly = true)
    public List<AlertaStockResponse> listarAlertas(Long productoId) {
        List<AlertaStockEntity> alertas;

        if (productoId == null) {
            alertas = alertaRepository.findAllByOrderByFechaDesc();
        } else {
            if (!productoRepository.existsById(productoId)) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado");
            }

            alertas = alertaRepository
                    .findByProducto_IdOrderByFechaDesc(productoId);
        }

        return alertas.stream()
                .map(this::convertirAResponse)
                .toList();
    }

    private AlertaStockResponse convertirAResponse(AlertaStockEntity alerta) {
        return new AlertaStockResponse(
                alerta.getId(),
                alerta.getFecha(),
                alerta.getProducto().getId(),
                alerta.getProducto().getNombre(),
                alerta.getStockActual(),
                alerta.getUmbral(),
                alerta.getMensaje()
        );
    }
}