package ar.utn.frba.dds.modelos.entidades.incidentes;

import ar.utn.frba.dds.arquitectura.dtos.DTOHeladera;
import ar.utn.frba.dds.arquitectura.dtos.DTOProvincia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DisplayFallasTecnicas {
    private String id;
    private DTOHeladera heladera;
    private DTOProvincia provincia;
    private String direccion;
    private Integer codigo_postal;
    private LocalDateTime fechaYHoraFalla;
    private LocalDateTime fechaYHoraReporte;
    private TipoIncidente tipoIncidente;
}
