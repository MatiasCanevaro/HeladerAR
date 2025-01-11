package ar.utn.frba.dds.arquitectura.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DTOVianda {
    private String id;
    private String nombreComida;
    private LocalDate fechaCaducidad;
    private Double calorias;
    private Double peso;
}
