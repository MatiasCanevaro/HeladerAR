package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOCuestionarioRespondido;
import ar.utn.frba.dds.modelos.entidades.cuestionarios.CuestionarioRespondido;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOCuestionarioRespondido {
    public static List<DTOCuestionarioRespondido> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOCuestionarioRespondido::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOCuestionarioRespondido convertirADTO(Object config) {
        CuestionarioRespondido cuestionarioRespondido = (CuestionarioRespondido) config;
        if(cuestionarioRespondido!=null){
            return new DTOCuestionarioRespondido(
                    cuestionarioRespondido.getId(),
                    MapperDTOCuestionario.convertirADTO(cuestionarioRespondido.getCuestionario()),
                    cuestionarioRespondido.getFechaYHoraRespondido()
            );
        }
        return null;
    }
}
