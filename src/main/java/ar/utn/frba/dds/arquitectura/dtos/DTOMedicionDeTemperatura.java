package ar.utn.frba.dds.arquitectura.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DTOMedicionDeTemperatura {
    private String id;
    private Double valor;
    private LocalDateTime fechaYHoraMedicion;
}
