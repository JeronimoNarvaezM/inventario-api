
package co.edu.autonoma.inventarioapi.service;

import co.edu.autonoma.inventarioapi.dto.MovimientoRequest;
import co.edu.autonoma.inventarioapi.dto.MovimientoResponse;
import co.edu.autonoma.inventarioapi.entity.MovimientoEntity;
import co.edu.autonoma.inventarioapi.entity.ProductoEntity;
import co.edu.autonoma.inventarioapi.entity.Rol;
import co.edu.autonoma.inventarioapi.entity.TipoMovimiento;
import co.edu.autonoma.inventarioapi.entity.UsuarioEntity;
import co.edu.autonoma.inventarioapi.repository.MovimientoRepository;
import co.edu.autonoma.inventarioapi.repository.ProductoRepository;
import co.edu.autonoma.inventarioapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlertaService alertaService;

    @Transactional(readOnly = true)
    public List<MovimientoResponse> listarPorProducto(Long productoId) {
        if (productoId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El ID del producto es obligatorio"
            );
        }

        if (!productoRepository.existsById(productoId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Producto no encontrado");
        }

        return movimientoRepository
                .findByProducto_IdOrderByFechaDesc(productoId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional
    public MovimientoResponse registrarMovimiento(MovimientoRequest request) {
        if (request == null
                || request.tipo() == null
                || request.cantidad() == null
                || request.cantidad() <= 0
                || request.productoId() == null
                || request.usuarioId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El tipo, producto, usuario y una cantidad positiva son obligatorios"
            );
        }

        // Bloquea la fila del producto hasta terminar la transacción:
        // si llegan dos salidas a la vez, la segunda espera a la primera
        // y lee el stock ya actualizado (protege la RN-01)
        ProductoEntity producto = productoRepository
                .buscarParaActualizar(request.productoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado"));

        UsuarioEntity usuario = usuarioRepository
                .findById(request.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        validarUsuario(usuario);

        if (!producto.isActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se pueden registrar movimientos para un producto inactivo"
            );
        }

        int stockActual = producto.getStockActual();
        int cantidad = request.cantidad();
        int nuevoStock;

        if (request.tipo() == TipoMovimiento.ENTRADA) {
            long resultado = (long) stockActual + cantidad;

            if (resultado > Integer.MAX_VALUE) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "La cantidad supera el stock máximo permitido"
                );
            }

            nuevoStock = (int) resultado;

        } else if (request.tipo() == TipoMovimiento.SALIDA) {
            if (cantidad > stockActual) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Stock insuficiente para registrar la salida"
                );
            }

            nuevoStock = stockActual - cantidad;

        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El tipo debe ser ENTRADA o SALIDA"
            );
        }

        producto.setStockActual(nuevoStock);
        productoRepository.save(producto);

        MovimientoEntity movimiento = new MovimientoEntity();
        movimiento.setTipo(request.tipo());
        movimiento.setCantidad(cantidad);
        movimiento.setProducto(producto);
        movimiento.setUsuario(usuario);
        movimiento.setObservacion(request.observacion());

        MovimientoEntity guardado = movimientoRepository.save(movimiento);

        // Solo una salida puede dejar el stock bajo el umbral
        if (request.tipo() == TipoMovimiento.SALIDA) {
            alertaService.evaluarStock(producto);
        }

        return convertirAResponse(guardado);
    }

    // Solo un usuario activo con rol AUXILIAR_BODEGA puede registrar movimientos
    private void validarUsuario(UsuarioEntity usuario) {
        if (!usuario.isActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El usuario está inactivo y no puede registrar movimientos"
            );
        }

        if (usuario.getRol() != Rol.AUXILIAR_BODEGA) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Solo un auxiliar de bodega puede registrar movimientos"
            );
        }
    }

    private MovimientoResponse convertirAResponse(
            MovimientoEntity movimiento) {
        return new MovimientoResponse(
                movimiento.getId(),
                movimiento.getTipo(),
                movimiento.getCantidad(),
                movimiento.getFecha(),
                movimiento.getUsuario().getId(),
                movimiento.getUsuario().getNombre(),
                movimiento.getObservacion(),
                movimiento.getProducto().getId(),
                movimiento.getProducto().getNombre()
        );
    }
}
