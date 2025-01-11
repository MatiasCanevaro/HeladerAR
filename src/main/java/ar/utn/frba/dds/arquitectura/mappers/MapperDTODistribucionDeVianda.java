package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTODistribucionDeVianda;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DistribucionDeVianda;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTODistribucionDeVianda {
    public static DTODistribucionDeVianda convertirADTO(DistribucionDeVianda distribucionDeVianda) {
        return new DTODistribucionDeVianda(
                distribucionDeVianda.getNombre(),
                MapperDTOHeladera.convertirADTO(distribucionDeVianda.getHeladeraOrigen()),
                MapperDTOHeladera.convertirADTO(distribucionDeVianda.getHeladeraDestino()),
                distribucionDeVianda.getCantViandasAMover(),
                MapperDTOMotivoDistribucion.convertirADTO(distribucionDeVianda.getMotivoDistribucion()),
                distribucionDeVianda.getFechaDistribucion(),
                MapperDTOPersonaFisica.convertirADTO(distribucionDeVianda.getPersonaFisica())
        );
    }

    public static List<DTODistribucionDeVianda> convertirListaADTOs(List<DistribucionDeVianda> distribuciones) {
        return distribuciones.stream()
                .map(MapperDTODistribucionDeVianda::convertirADTO)
                .collect(Collectors.toList());
    }
}