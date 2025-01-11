package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTODistribucionDeTarjeta;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DistribucionDeTarjeta;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTODistribucionDeTarjeta {
    public static DTODistribucionDeTarjeta convertirADTO(DistribucionDeTarjeta distribucionDeTarjeta) {
        return new DTODistribucionDeTarjeta(
                distribucionDeTarjeta.getNombre(),
                MapperDTOTarjeta.convertirADTO(distribucionDeTarjeta.getTarjeta()),
                MapperDTOPersonaFisica.convertirADTO(distribucionDeTarjeta.getPersonaFisicaQueLaRegistro()),
                MapperDTOPersonaVulnerable.convertirADTO(distribucionDeTarjeta.getPersonaVulnerable()),
                distribucionDeTarjeta.getFechaEntregaTarjeta()
        );
    }

    public static List<DTODistribucionDeTarjeta> convertirListaADTOs(List<DistribucionDeTarjeta> distribuciones) {
        return distribuciones.stream()
                .map(MapperDTODistribucionDeTarjeta::convertirADTO)
                .collect(Collectors.toList());
    }
}