package ar.utn.frba.dds.modelos.entidades.incidentes;

import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.TipoDocumento;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class FallaTecnicaInfo {
    private LocalDateTime fechaYHoraFalla;
    private Heladera heladera;
    private PersonaFisica personaFisicaQueLoReporto;
    private String descripcion;
    private String pathImagen;
    private TipoIncidente tipoIncidente;

    public static FallaTecnicaInfo crearFallaTecnicaInfo(
            LocalDateTime fechaYHoraFalla,Heladera heladera,PersonaFisica personaFisica,
            String descripcion,String pathImagen, TipoIncidente tipoIncidente){
        return FallaTecnicaInfo
                .builder()
                .fechaYHoraFalla(fechaYHoraFalla)
                .heladera(heladera)
                .personaFisicaQueLoReporto(personaFisica)
                .descripcion(descripcion)
                .pathImagen(pathImagen)
                .tipoIncidente(tipoIncidente)
                .build();
    }
}
