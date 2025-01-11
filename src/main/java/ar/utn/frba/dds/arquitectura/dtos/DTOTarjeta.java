package ar.utn.frba.dds.arquitectura.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class DTOTarjeta {
    private String id;
    private String codigoAlfanumerico;
    private DTOPersonaVulnerable personaVulnerable;
    private DTOPersonaFisica personaFisica;
    private List<LocalDateTime> fechasRetirosDeViandas;
    private Integer cantidadDeVecesQuePuedeSerUtilizadaPorDia;
}
