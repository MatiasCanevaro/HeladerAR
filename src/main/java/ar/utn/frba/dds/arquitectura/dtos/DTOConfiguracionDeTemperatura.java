package ar.utn.frba.dds.arquitectura.dtos;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DTOConfiguracionDeTemperatura {
    private String id;
    private Double temperaturaMaximaConfigurada;
    private Double temperaturaMinimaConfigurada;
    private DTOPersonaFisica personaFisicaQueLaConfiguro;
    private LocalDateTime fechaYHoraConfiguracion;
}
