package ar.utn.frba.dds.arquitectura.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;



@Getter
@AllArgsConstructor
public class DTOModeloDeHeladera {
    private String id;
    private String nombre;
    private Integer capacidadEnViandas;
    private Double temperaturaMinimaAceptable;
    private Double temperaturaMaximaAceptable;
}
