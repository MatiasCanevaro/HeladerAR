package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOCiudad;
import ar.utn.frba.dds.arquitectura.dtos.DTOMotivoDistribucion;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.MotivoDistribucion;
import ar.utn.frba.dds.modelos.entidades.geografia.Ciudad;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOMotivoDistribucion {
    public static List<DTOMotivoDistribucion> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOMotivoDistribucion::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOMotivoDistribucion convertirADTO(Object objeto){
        MotivoDistribucion motivoDistribucion = (MotivoDistribucion) objeto;
        if(motivoDistribucion!=null){
            return new DTOMotivoDistribucion(
                    motivoDistribucion.getNombre()
            );
        }
        return null;
    }
}
