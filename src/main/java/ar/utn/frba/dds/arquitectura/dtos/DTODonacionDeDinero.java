package ar.utn.frba.dds.arquitectura.dtos;

import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.FormaDeColaboracion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
@Getter
@AllArgsConstructor
public class DTODonacionDeDinero {
    private FormaDeColaboracion nombre;
    private DTOPersonaFisica personaFisica;
    private DTOPersonaJuridica personaJuridica;
    private LocalDate fechaDonacion;
    private Double monto;
    private Integer frecuenciaEnDia;
}