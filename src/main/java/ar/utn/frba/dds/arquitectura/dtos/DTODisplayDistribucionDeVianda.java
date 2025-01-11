package ar.utn.frba.dds.arquitectura.dtos;

import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.FormaDeColaboracion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DTODisplayDistribucionDeVianda {
    private String id;
    private DTOProvincia provincia;
    private String direccion;
    private Integer codigo_postal;
    private Integer cantidadViandasDisponibles;
    private Boolean estaActiva;
}
