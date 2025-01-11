package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOTecnico;
import ar.utn.frba.dds.modelos.entidades.personas.Tecnico;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOTecnico {
    public static List<DTOTecnico> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOTecnico::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOTecnico convertirADTO(Object objeto){
        Tecnico tecnico = (Tecnico) objeto;
        return new DTOTecnico(
                tecnico.getId(),
                tecnico.getNombre(),
                tecnico.getApellido(),
                tecnico.getNumeroDocumento(),
                tecnico.getTipoDocumento(),
                tecnico.getCuil(),
                tecnico.getPuntoEstrategico()
        );
    }
}
