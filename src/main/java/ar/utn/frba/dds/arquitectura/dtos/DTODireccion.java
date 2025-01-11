package ar.utn.frba.dds.arquitectura.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DTODireccion {
    private String id;
    private String calle;
    private String altura;
    private String piso;
    private String codigoPostal;
    private DTOCiudad ciudad;
}
