package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOReporte;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOReporte {
    /*
    public static List<DTOReporte> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOReporte::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOReporte convertirADTO(Object reporte) {
        Reporte reporte = (Reporte) reporte;
        return new DTOReporte(
                personaReal.getId(),
                personaReal.getNombre(),
        );
    }*/
}
