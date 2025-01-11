package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOCiudad;
import ar.utn.frba.dds.arquitectura.dtos.DTODireccion;
import ar.utn.frba.dds.arquitectura.dtos.DTOProvincia;
import ar.utn.frba.dds.arquitectura.dtos.DTOPuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.geografia.Ciudad;
import ar.utn.frba.dds.modelos.entidades.geografia.Direccion;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOCiudad {
    public static List<DTOCiudad> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOCiudad::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOCiudad convertirADTO(Object objeto){
        Ciudad ciudad = (Ciudad) objeto;
        return new DTOCiudad(
                ciudad.getId(),
                ciudad.getNombre(),
                //TODO comentado por referencia circular MapperDTOPuntoEstrategico.convertirADTO(ciudad.getCentroDeLaCiudad()),
                MapperDTOProvincia.convertirADTO(ciudad.getProvincia())
        );
    }
}
