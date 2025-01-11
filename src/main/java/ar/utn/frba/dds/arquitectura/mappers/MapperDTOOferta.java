package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOHeladera;
import ar.utn.frba.dds.arquitectura.dtos.DTOOferta;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Oferta;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOOferta {
    public static List<DTOOferta> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOOferta::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOOferta convertirADTO(Object objeto){
        Oferta ofertaReal = (Oferta) objeto;
        return new DTOOferta(
                ofertaReal.getId(),
                ofertaReal.getNombre(),
                ofertaReal.getDescripcion(),
                ofertaReal.getPathImagen(),
                ofertaReal.getCantidadDePuntosNecesariosParaAccederAlBeneficio(),
                MapperDTORubro.convertirADTO(ofertaReal.getRubro())
        );
    }
}
