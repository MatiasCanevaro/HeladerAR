package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTODisplayFallasTecnicas;
import ar.utn.frba.dds.arquitectura.dtos.DTOFallasTecnicas;
import ar.utn.frba.dds.modelos.entidades.incidentes.DisplayFallasTecnicas;
import ar.utn.frba.dds.modelos.entidades.incidentes.FallaTecnica;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOFallasTecnicas {
    public static List<DTOFallasTecnicas> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOFallasTecnicas::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOFallasTecnicas convertirADTO(Object objeto){
        FallaTecnica falla = (FallaTecnica) objeto;
        return new DTOFallasTecnicas(
                falla.getId(),
                falla.getFechaYHoraFalla(),
                falla.getFechaYHoraReporte(),
                MapperDTOPuntoEstrategico.convertirADTO(falla.getPuntoEstrategico()),
                MapperDTOHeladera.convertirADTO(falla.getHeladera()),
                falla.getEstaSolucionado(),
                falla.getTipoIncidente(),
                falla.getFechaYHoraFueSolucionado(),
                MapperDTOPersonaFisica.convertirADTO(falla.getPersonaFisicaQueLoReporto()),
                falla.getDescripcion(),
                falla.getPathImagen()
                );
    }
}
