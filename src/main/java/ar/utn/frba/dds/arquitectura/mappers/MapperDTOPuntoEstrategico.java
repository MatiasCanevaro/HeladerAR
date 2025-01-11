package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOPuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOPuntoEstrategico {
    public static List<DTOPuntoEstrategico> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOPuntoEstrategico::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOPuntoEstrategico convertirADTO(Object objeto) {
        PuntoEstrategico puntoEstrategico = (PuntoEstrategico) objeto;
        return new DTOPuntoEstrategico(
                puntoEstrategico.getId(),
                puntoEstrategico.getNombre(),
                puntoEstrategico.getLatitud(),
                puntoEstrategico.getLongitud(),
                MapperDTODireccion.convertirADTO(puntoEstrategico.getDireccion())
        );
    }
}
