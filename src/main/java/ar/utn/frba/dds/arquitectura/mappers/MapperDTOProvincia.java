package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOCiudad;
import ar.utn.frba.dds.arquitectura.dtos.DTOProvincia;
import ar.utn.frba.dds.modelos.entidades.geografia.Ciudad;
import ar.utn.frba.dds.modelos.entidades.geografia.Provincia;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOProvincia {
    public static List<DTOProvincia> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOProvincia::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOProvincia convertirADTO(Object objeto){
        if(objeto == null) {
            return null;
        }
        Provincia provincia = (Provincia) objeto;
        return new DTOProvincia(
                provincia.getId(),
                provincia.getNombre()
        );
    }
}
