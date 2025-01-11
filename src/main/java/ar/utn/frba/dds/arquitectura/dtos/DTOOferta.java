package ar.utn.frba.dds.arquitectura.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;



@Getter
@AllArgsConstructor
public class DTOOferta {
    private String id;
    private String nombre;
    private String descripcion;
    private String pathImagen;
    private Double cantidadDePuntosNecesariosParaAccederAlBeneficio;
    private DTORubro dtoRubro;
}
