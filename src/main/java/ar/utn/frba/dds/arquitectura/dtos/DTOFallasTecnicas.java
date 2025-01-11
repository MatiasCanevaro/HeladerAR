package ar.utn.frba.dds.arquitectura.dtos;

import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.incidentes.TipoIncidente;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DTOFallasTecnicas {
    private String id;
    private LocalDateTime fechaYHoraFalla;
    private LocalDateTime fechaYHoraReporte;
    private DTOPuntoEstrategico puntoEstrategico;
    private DTOHeladera heladera;
    private Boolean estaSolucionado;
    private TipoIncidente tipoIncidente;
    private LocalDateTime fechaYHoraFueSolucionado;
    private DTOPersonaFisica personaFisicaQueLoReporto;
    private String descripcion;
    private String pathImagen;
}
