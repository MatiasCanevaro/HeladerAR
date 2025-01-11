package ar.utn.frba.dds.arquitectura.mappers;
import ar.utn.frba.dds.arquitectura.dtos.DTOCiudad;
import ar.utn.frba.dds.arquitectura.dtos.DTOEmail;
import ar.utn.frba.dds.modelos.entidades.personas.Email;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOEmail {
    public static List<DTOCiudad> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOCiudad::convertirADTO)
                .collect(Collectors.toList());

    }
    public static DTOEmail convertirADTO(Object objeto){
        Email email = (Email) objeto;
        return new DTOEmail(
                email.getCorreoElectronico()
        );
    }
}


