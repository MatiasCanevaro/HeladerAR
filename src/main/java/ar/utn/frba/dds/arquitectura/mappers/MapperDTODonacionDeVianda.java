package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTODonacionDeVianda;
import ar.utn.frba.dds.arquitectura.dtos.DTOVianda;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DonacionDeVianda;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTODonacionDeVianda {
    public static DTODonacionDeVianda convertirADTO(DonacionDeVianda donacionDeVianda) {
        return new DTODonacionDeVianda(
                donacionDeVianda.getNombre(),
                MapperDTOPersonaFisica.convertirADTO(donacionDeVianda.getPersonaFisica()),
                MapperDTOVianda.convertirADTO(donacionDeVianda.getVianda()),
                donacionDeVianda.getFechaListaParaEntregar(),
                donacionDeVianda.getFechaDonacion()
        );
    }

    public static List<DTODonacionDeVianda> convertirListaADTOs(List<DonacionDeVianda> donaciones) {
        return donaciones.stream()
                .map(MapperDTODonacionDeVianda::convertirADTO)
                .collect(Collectors.toList());
    }
}