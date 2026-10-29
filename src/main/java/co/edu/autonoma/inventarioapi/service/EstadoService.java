package co.edu.autonoma.inventarioapi.service;

import org.springframework.stereotype.Service;
import co.edu.autonoma.inventarioapi.dto.EstadoResponse;

@Service
public class EstadoService {

    public EstadoResponse consultarEstado(){
        return new EstadoResponse(
                "reservas-api",
                "disponible"
        );
    }
}