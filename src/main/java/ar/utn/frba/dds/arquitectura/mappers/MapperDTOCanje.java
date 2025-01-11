package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOCanje;
import ar.utn.frba.dds.arquitectura.dtos.DTOCiudad;
import ar.utn.frba.dds.modelos.entidades.geografia.Ciudad;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Canje;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOCanje {
    public static List<DTOCanje> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOCanje::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOCanje convertirADTO(Object objeto){
        Canje canje = (Canje) objeto;
        return new DTOCanje(
                canje.getId(),
                canje.getFechaYHoraCanje(),
                MapperDTOOferta.convertirADTO(canje.getOfertaCanjeada()),
                MapperDTOPersonaJuridica.convertirADTO(canje.getPersonaJuridicaQueOfrecio()),
                canje.getPersonaFisicaQueCanjeo() != null ? MapperDTOPersonaFisica.convertirADTO(canje.getPersonaFisicaQueCanjeo()) : null,
                canje.getPersonaJuridicaQueCanjeo() != null ? MapperDTOPersonaJuridica.convertirADTO(canje.getPersonaJuridicaQueCanjeo()) : null

        );
    }
}
