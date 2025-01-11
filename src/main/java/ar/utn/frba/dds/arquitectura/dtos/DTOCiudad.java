package ar.utn.frba.dds.arquitectura.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DTOCiudad {
    private String id;
    private String nombre;
    //TODO comentado por referencia circular private DTOPuntoEstrategico centroDeLaCiudad;
    private DTOProvincia provincia;
}
