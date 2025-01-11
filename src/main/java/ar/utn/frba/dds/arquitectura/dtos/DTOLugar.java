package ar.utn.frba.dds.arquitectura.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DTOLugar {
    private String id;
    private String nombre;
    private Double latitud;
    private Double longitud;
    private Double distanciaEnKM;
}