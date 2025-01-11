package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOHeladera;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOHeladera {
    public static List<DTOHeladera> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOHeladera::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOHeladera convertirADTO(Object objeto) {
        Heladera heladeraReal = (Heladera) objeto;
        if (heladeraReal!=null) {
            return new DTOHeladera(
                    heladeraReal.getId(),
                    MapperDTOVianda.convertirListaADTOs(new ArrayList<>(heladeraReal.getViandas())),
                    MapperDTOPuntoEstrategico.convertirADTO(heladeraReal.getPuntoEstrategico()),
                    MapperDTOModeloDeHeladera.convertirADTO(heladeraReal.getModelo()),
                    MapperDTOConfiguracionDeTemperatura.convertirListaADTOs(new ArrayList<>(heladeraReal.
                            getConfiguracionesDeTemperaturas())),
                    MapperDTOMedicionDeTemperatura.convertirListaADTOs(
                            new ArrayList<>(heladeraReal.getMedicionesDeTemperatura())),
                    heladeraReal.getFechasYHorasDejoDeEstarActiva(),
                    heladeraReal.getFechasYHorasVolvioAEstarActiva(),
                    heladeraReal.getFechasYHorasReubicada(),
                    heladeraReal.getEstaActiva()
            );
        }
        return null;
    }
}

