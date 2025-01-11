package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOConfiguracionDeTemperatura;
import ar.utn.frba.dds.modelos.entidades.heladeras.ConfiguracionDeTemperatura;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOConfiguracionDeTemperatura {
    public static List<DTOConfiguracionDeTemperatura> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOConfiguracionDeTemperatura::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOConfiguracionDeTemperatura convertirADTO(Object config) {
        ConfiguracionDeTemperatura configReal = (ConfiguracionDeTemperatura) config;
        return new DTOConfiguracionDeTemperatura(
                configReal.getId(),
                configReal.getTemperaturaMaximaConfigurada(),
                configReal.getTemperaturaMinimaConfigurada(),
                MapperDTOPersonaFisica.convertirADTO(configReal.getPersonaFisicaQueLaConfiguro()),
                configReal.getFechaYHoraConfiguracion()
        );
    }
}
