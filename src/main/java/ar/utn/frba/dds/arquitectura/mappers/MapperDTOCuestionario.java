package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOCuestionario;
import ar.utn.frba.dds.modelos.entidades.cuestionarios.Cuestionario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOCuestionario {
    public static List<DTOCuestionario> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOCuestionario::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOCuestionario convertirADTO(Object objeto) {
        Cuestionario cuestionario = (Cuestionario) objeto;
        return new DTOCuestionario(
                cuestionario.getId(),
                cuestionario.getNombre(),
                MapperDTOPregunta.convertirListaADTOs(new ArrayList<>(cuestionario.getPreguntas()))
        );
    }
}
