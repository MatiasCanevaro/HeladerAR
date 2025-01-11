package ar.utn.frba.dds.arquitectura.dtos;

import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Oferta;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DTOCanje {
    private String id;
    private LocalDateTime fechaYHoraCanje;
    private DTOOferta ofertaCanjeada;
    private DTOPersonaJuridica personaJuridicaQueOfrecio;
    private DTOPersonaFisica personaFisicaQueCanjeo;
    private DTOPersonaJuridica personaJuridicaQueCanjeo;
}
