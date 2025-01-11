package ar.utn.frba.dds.arquitectura.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DTOPuntoEstrategico {
    private String id;
    private String nombre;
    private String latitud;
    private String longitud;
    private DTODireccion direccion;
}
