package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOCiudad;
import ar.utn.frba.dds.arquitectura.dtos.DTODireccion;
import ar.utn.frba.dds.arquitectura.dtos.DTOOferta;
import ar.utn.frba.dds.modelos.entidades.geografia.Direccion;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTODireccion {
    public static List<DTODireccion> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTODireccion::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTODireccion convertirADTO(Object objeto){
        Direccion direccion = (Direccion) objeto;
        if(direccion!=null){
            return new DTODireccion(
                    direccion.getId(),
                    direccion.getCalle(),
                    direccion.getAltura(),
                    direccion.getPiso(),
                    direccion.getCodigoPostal(),
                    MapperDTOCiudad.convertirADTO(direccion.getCiudad())
            );
        }
        else{
            return null;
        }
    }
}
