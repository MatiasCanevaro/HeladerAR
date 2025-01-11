package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOErroresValidacion;
import lombok.Getter;

import java.util.List;


public class MapperDTOErroresValidacion {
    public static DTOErroresValidacion convertirListaADTO(List<String> errores) {
        return new DTOErroresValidacion(errores);
    }
}
