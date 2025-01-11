package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTODisplayConsultarHeladerasActivas;
import ar.utn.frba.dds.arquitectura.dtos.DTODisplayFallasTecnicas;
import ar.utn.frba.dds.modelos.entidades.heladeras.DisplayConsultarHeladerasActivas;
import ar.utn.frba.dds.modelos.entidades.incidentes.DisplayFallasTecnicas;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTODisplayFallasTecnicas {
    public static List<DTODisplayFallasTecnicas> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTODisplayFallasTecnicas::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTODisplayFallasTecnicas convertirADTO(Object objeto){
        DisplayFallasTecnicas falla = (DisplayFallasTecnicas) objeto;
        return new DTODisplayFallasTecnicas(
                falla.getId(),
                MapperDTOHeladera.convertirADTO(falla.getHeladera()),
                MapperDTOProvincia.convertirADTO(falla.getProvincia()),
                falla.getDireccion(),
                falla.getCodigo_postal(),
                falla.getTipoIncidente().name(),
                falla.getFechaYHoraFalla().toString(),
                falla.getFechaYHoraReporte().toString()
                );
    }
}
