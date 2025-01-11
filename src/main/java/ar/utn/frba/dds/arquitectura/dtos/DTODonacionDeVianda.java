package ar.utn.frba.dds.arquitectura.dtos;

import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.FormaDeColaboracion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
@AllArgsConstructor
@Getter
public class DTODonacionDeVianda {
    private FormaDeColaboracion nombre;
    private DTOPersonaFisica personaFisica;
    private DTOVianda vianda;
    private LocalDate fechaListaParaEntregar;
    private LocalDate fechaDonacion;
}
