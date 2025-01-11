package ar.utn.frba.dds.arquitectura.dtos;

import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.FormaDeColaboracion;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaVulnerable;
import ar.utn.frba.dds.modelos.entidades.tarjetas.Tarjeta;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DTODistribucionDeTarjeta {
    private FormaDeColaboracion nombre;
    private DTOTarjeta tarjeta;
    private DTOPersonaFisica personaFisicaQueLaRegistro;
    private DTOPersonaVulnerable personaVulnerable;
    private LocalDate fechaEntregaTarjeta;
}