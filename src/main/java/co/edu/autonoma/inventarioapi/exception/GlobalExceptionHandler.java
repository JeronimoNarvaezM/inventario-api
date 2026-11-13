package co.edu.autonoma.inventarioapi.exception;

import co.edu.autonoma.inventarioapi.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Errores que lanzamos nosotros en los servicios (400, 403, 404, 409...)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorResponse> manejarResponseStatus(
            ResponseStatusException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        String mensaje = ex.getReason() != null
                ? ex.getReason()
                : status.getReasonPhrase();

        return construirRespuesta(status, mensaje, request);
    }

    // JSON mal formado o valor de enum inválido (ej. tipo "TRASLADO")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> manejarJsonInvalido(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                "El cuerpo de la petición es inválido o contiene un valor no permitido "
                        + "(por ejemplo, un tipo de movimiento distinto de ENTRADA o SALIDA)",
                request
        );
    }

    // Falta un parámetro obligatorio (ej. productoId en el kardex)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> manejarParametroFaltante(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {

        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                "Falta el parámetro obligatorio: " + ex.getParameterName(),
                request
        );
    }

    // Parámetro con tipo incorrecto (ej. productoId=abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> manejarTipoIncorrecto(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                "El valor del parámetro '" + ex.getName() + "' no es válido",
                request
        );
    }

    // Arma la respuesta con el mismo formato para todos los errores
    private ResponseEntity<ApiErrorResponse> construirRespuesta(
            HttpStatus status, String mensaje, HttpServletRequest request) {

        ApiErrorResponse cuerpo = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensaje,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(cuerpo);
    }
}