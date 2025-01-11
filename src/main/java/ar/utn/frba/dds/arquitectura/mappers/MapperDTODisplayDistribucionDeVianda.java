package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTODireccion;
import ar.utn.frba.dds.arquitectura.dtos.DTODisplayDistribucionDeVianda;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DisplayDistribucionDeVianda;
import ar.utn.frba.dds.modelos.entidades.geografia.Direccion;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTODisplayDistribucionDeVianda {
    public static List<DTODisplayDistribucionDeVianda> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTODisplayDistribucionDeVianda::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTODisplayDistribucionDeVianda convertirADTO(Object objeto){
        DisplayDistribucionDeVianda distribucion = (DisplayDistribucionDeVianda) objeto;
        return new DTODisplayDistribucionDeVianda(
                distribucion.getId(),
                MapperDTOProvincia.convertirADTO(distribucion.getProvincia()),
                distribucion.getDireccion(),
                distribucion.getCodigo_postal(),
                distribucion.getCantidadViandasDisponibles(),
                distribucion.getEstaActiva()
        );
    }
}
