package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTODonacionDeDinero;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DonacionDeDinero;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTODonacionDeDinero {
    public static DTODonacionDeDinero convertirADTO(DonacionDeDinero donacionDeDinero) {
        return new DTODonacionDeDinero(
                donacionDeDinero.getNombre(),
                MapperDTOPersonaFisica.convertirADTO(donacionDeDinero.getPersonaFisica()),
                MapperDTOPersonaJuridica.convertirADTO(donacionDeDinero.getPersonaJuridica()),
                donacionDeDinero.getFechaDonacion(),
                donacionDeDinero.getMonto(),
                donacionDeDinero.getFrecuenciaEnDia()
        );
    }

    public static List<DTODonacionDeDinero> convertirListaADTOs(List<DonacionDeDinero> donaciones) {
        return donaciones.stream()
                .map(MapperDTODonacionDeDinero::convertirADTO)
                .collect(Collectors.toList());
    }
}