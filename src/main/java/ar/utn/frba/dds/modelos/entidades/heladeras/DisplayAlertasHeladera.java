package ar.utn.frba.dds.modelos.entidades.heladeras;

import ar.utn.frba.dds.arquitectura.dtos.DTOModeloDeHeladera;
import ar.utn.frba.dds.arquitectura.dtos.DTOProvincia;
import ar.utn.frba.dds.modelos.entidades.incidentes.TipoIncidente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DisplayAlertasHeladera {
    private String id;
    private DTOProvincia provincia;
    private String direccion;
    private DTOModeloDeHeladera modelo;
    private TipoIncidente tipoIncidente;
    private LocalDateTime ultima_vez_activa;
}
