package ar.utn.frba.dds.modelos.entidades.formasDeColaboracion;

import ar.utn.frba.dds.arquitectura.dtos.DTODireccion;
import ar.utn.frba.dds.arquitectura.dtos.DTOProvincia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DisplayDistribucionDeVianda {
    private String id;
    private DTOProvincia provincia;
    private String direccion;
    private Integer codigo_postal;
    private Integer cantidadViandasDisponibles;
    private Boolean estaActiva;
}
