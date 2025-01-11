package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOAlertasHeladera;
import ar.utn.frba.dds.arquitectura.dtos.DTOFallasTecnicas;
import ar.utn.frba.dds.modelos.entidades.incidentes.AlertaHeladera;
import ar.utn.frba.dds.modelos.entidades.incidentes.FallaTecnica;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOAlertasHeladera {
    public static List<DTOAlertasHeladera> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOAlertasHeladera::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOAlertasHeladera convertirADTO(Object objeto){
        AlertaHeladera alertaHeladera = (AlertaHeladera) objeto;
        DTOAlertasHeladera dtoAlertasHeladera = new DTOAlertasHeladera(
                alertaHeladera.getId(),
                alertaHeladera.getTipoIncidente(),
                alertaHeladera.getFechaYHora(),
                MapperDTOPuntoEstrategico.convertirADTO(alertaHeladera.getPuntoEstrategico()),
                MapperDTOHeladera.convertirADTO(alertaHeladera.getHeladera()),
                alertaHeladera.getEstaSolucionado(),
                alertaHeladera.getFechaYHoraFueSolucionado(),
                MapperDTOTecnico.convertirListaADTOs(alertaHeladera.getTecnicosSugeridos().stream().map(Object.class::cast).collect(Collectors.toList())),
                null
        );
        if (alertaHeladera.getTecnicoQueAcepto() != null){
            dtoAlertasHeladera.setTecnicoQueAcepto(MapperDTOTecnico.convertirADTO(alertaHeladera.getTecnicoQueAcepto()));
        }
        return dtoAlertasHeladera;
    }
}
