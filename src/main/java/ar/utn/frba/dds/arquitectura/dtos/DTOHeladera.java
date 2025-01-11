package ar.utn.frba.dds.arquitectura.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DTOHeladera {
    private String id;
    private List<DTOVianda> viandas;
    private DTOPuntoEstrategico puntoEstrategico;
    private DTOModeloDeHeladera modelo;
    private List<DTOConfiguracionDeTemperatura> configuracionesDeTemperaturas;
    private List<DTOMedicionDeTemperatura> medicionesDeTemperatura;
    private List<LocalDateTime> fechasYHorasDejoDeEstarActiva;
    private List<LocalDateTime> fechasYHorasVolvioAEstarActiva;
    private List<LocalDateTime> fechasYHorasReubicada;
    private Boolean estaActiva;
}
