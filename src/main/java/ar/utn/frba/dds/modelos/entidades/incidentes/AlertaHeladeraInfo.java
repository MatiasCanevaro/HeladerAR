package ar.utn.frba.dds.modelos.entidades.incidentes;

import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
public class AlertaHeladeraInfo {
    private Heladera heladera;
    private TipoIncidente tipoIncidente;

    public static AlertaHeladeraInfo crearAlertaHeladeraInfo(Heladera heladera, TipoIncidente tipoIncidente){
        return AlertaHeladeraInfo
                .builder()
                .heladera(heladera)
                .tipoIncidente(tipoIncidente)
                .build();
    }
}
