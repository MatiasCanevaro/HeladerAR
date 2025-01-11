package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOOpcion;
import ar.utn.frba.dds.modelos.entidades.cuestionarios.Opcion;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOOpcion {
    public static List<DTOOpcion> convertirListaADTOs(List<Opcion> opciones) {
        return opciones.stream()
                .map(MapperDTOOpcion::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOOpcion convertirADTO(Opcion opcion) {
        return new DTOOpcion(
                opcion.getId(),
                opcion.getNombre()
        );
    }
}