package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOModeloDeHeladera;
import ar.utn.frba.dds.modelos.entidades.heladeras.ModeloDeHeladera;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOModeloDeHeladera {
    public static List<DTOModeloDeHeladera> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOModeloDeHeladera::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOModeloDeHeladera convertirADTO(Object modelo) {
        ModeloDeHeladera modeloReal = (ModeloDeHeladera) modelo;
        return new DTOModeloDeHeladera(
                modeloReal.getId(),
                modeloReal.getNombre(),
                modeloReal.getCapacidadEnViandas(),
                modeloReal.getTemperaturaMinimaAceptable(),
                modeloReal.getTemperaturaMaximaAceptable()
        );
    }
}
