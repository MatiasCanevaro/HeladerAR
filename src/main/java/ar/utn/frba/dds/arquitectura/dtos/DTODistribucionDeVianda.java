package ar.utn.frba.dds.arquitectura.dtos;

import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.FormaDeColaboracion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
@AllArgsConstructor
@Getter
public class DTODistribucionDeVianda {
    private FormaDeColaboracion nombre;
    private DTOHeladera heladeraOrigen;
    private DTOHeladera heladeraDestino;
    private Integer cantViandasAMover;
    private DTOMotivoDistribucion motivoDistribucion;
    private LocalDate fechaDistribucion;
    private DTOPersonaFisica personaFisica;
}
