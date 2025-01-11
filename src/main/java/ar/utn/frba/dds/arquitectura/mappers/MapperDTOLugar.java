package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOLugar;
import ar.utn.frba.dds.modulos.recomendadoraDeLugaresDeDonacion.entities.Lugar;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOLugar {

    public static List<DTOLugar> convertirListaADTOs(List<Lugar> lugares) {
        return lugares.stream()
                .map(MapperDTOLugar::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOLugar convertirADTO(Lugar lugar) {
        return new DTOLugar(
                String.valueOf(lugar.getId()),
                lugar.getNombre(),
                lugar.getLat(),
                lugar.getLon(),
                lugar.getDistanciaEnKM()
        );
    }
}